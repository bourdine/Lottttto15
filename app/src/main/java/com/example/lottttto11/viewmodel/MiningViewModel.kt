package com.example.lottttto11.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.*
import com.example.lottttto11.mining.MergedMiningEngine
import com.example.lottttto11.mining.MiningMode
import com.example.lottttto11.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MiningViewModel(
    private val userId: Int,
    private val walletDao: WalletDao,
    private val userDao: UserDao,
    private val feeDao: FeeDao,                    // <-- добавили
    private val context: Context
) : ViewModel() {

    data class MiningState(
        val coin: Coin,
        val mode: MiningMode,
        val isActive: Boolean,
        val hashrate: Double,
        val totalHashes: Long,
        val shares: Int = 0,
        val blocksFound: Int = 0,
        val temperature: Float = 0f,
        val poolUrl: String = ""
    )

    private val _miningStates = MutableStateFlow<Map<Coin, MiningState>>(emptyMap())
    val miningStates: StateFlow<Map<Coin, MiningState>> = _miningStates.asStateFlow()

    private val _userHasSubscription = MutableStateFlow(false)
    val userHasSubscription: StateFlow<Boolean> = _userHasSubscription.asStateFlow()

    private lateinit var miningEngine: MergedMiningEngine

    init {
        viewModelScope.launch {
            val user = userDao.getUserById(userId)
            _userHasSubscription.value = user?.subscriptionActive == true
        }
        miningEngine = MergedMiningEngine(context, userId,
            onBlockFound = { coin, block ->
                handleBlock(coin, block)
            },
            onStatsUpdate = { coin, stats ->
                updateStats(coin, stats)
            }
        )
    }

    fun startMining(coin: Coin, mode: MiningMode, poolUrl: String) {
        val wallet = walletDao.getWalletByCoin(coin.name) ?: return
        miningEngine.startMiningForCoin(coin, listOf(poolUrl), wallet.address)
        _miningStates.value = _miningStates.value.toMutableMap().apply {
            this[coin] = MiningState(
                coin = coin,
                mode = mode,
                isActive = true,
                hashrate = 0.0,
                totalHashes = 0,
                poolUrl = poolUrl
            )
        }
    }

    fun stopMining(coin: Coin) {
        miningEngine.stopMiningForCoin(coin)
        _miningStates.value = _miningStates.value.toMutableMap().apply {
            this[coin] = this[coin]?.copy(isActive = false)
        }
    }

    fun stopAll() {
        miningEngine.stopAll()
        _miningStates.value = emptyMap()
    }

    private fun handleBlock(coin: Coin, block: MergedMiningData) {
        viewModelScope.launch {
            val current = _miningStates.value[coin]
            val reward = block.reward
            val fee = if (!_userHasSubscription.value) reward * 0.1 else 0.0
            val finalReward = reward - fee

            // Обновляем баланс пользователя
            val wallet = walletDao.getWalletByCoin(coin.name)
            if (wallet != null) {
                walletDao.updateBalance(wallet.id, wallet.balance + finalReward)

                // Если есть комиссия, сохраняем её в таблицу fees
                if (fee > 0) {
                    feeDao.insert(
                        Fee(
                            coin = coin.name,
                            amount = fee,
                            userWalletAddress = wallet.address,
                            blockHash = block.blockHash,
                            timestamp = block.timestamp
                        )
                    )
                }
            }

            // Уведомление, если стоимость блока >= 1 цент
            if (block.usdValue >= 0.01) {
                NotificationHelper.showBlockFoundNotification(context, coin, block.usdValue)
            }

            if (current != null) {
                _miningStates.value = _miningStates.value.toMutableMap().apply {
                    this[coin] = current.copy(blocksFound = current.blocksFound + 1)
                }
            }
        }
    }

    private fun updateStats(coin: Coin, stats: MergedMiningEngine.MiningStats) {
        val current = _miningStates.value[coin]
        if (current != null) {
            _miningStates.value = _miningStates.value.toMutableMap().apply {
                this[coin] = current.copy(
                    hashrate = stats.hashrate,
                    totalHashes = stats.totalHashes,
                    temperature = stats.temperature,
                    shares = stats.sharesSubmitted
                )
            }
        }
    }

    override fun onCleared() {
        miningEngine.stopAll()
        super.onCleared()
    }
}

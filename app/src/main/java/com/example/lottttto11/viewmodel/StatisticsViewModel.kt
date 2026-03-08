package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.BlockReward
import com.example.lottttto11.data.MiningSession
import com.example.lottttto11.data.UserDao
import com.example.lottttto11.data.WalletDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StatisticsData(
    val totalBlocks: Int = 0,
    val totalShares: Int = 0,
    val totalHashrate: Double = 0.0,
    val estimatedEarnings: Double = 0.0,
    val sessions: List<MiningSession> = emptyList(),
    val blocks: List<BlockReward> = emptyList()
)

class StatisticsViewModel(
    private val userId: Int,
    private val userDao: UserDao,
    private val walletDao: WalletDao
) : ViewModel() {

    private val _stats = MutableStateFlow(StatisticsData())
    val stats: StateFlow<StatisticsData> = _stats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            val totalBalance = walletDao.getTotalBalanceForUser(userId)
            _stats.value = StatisticsData(
                totalBlocks = 5,
                totalShares = 1234,
                totalHashrate = 4500.0,
                estimatedEarnings = totalBalance
            )
            _isLoading.value = false
        }
    }
}

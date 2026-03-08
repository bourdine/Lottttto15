package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.Wallet
import com.example.lottttto11.data.WalletDao
import com.example.lottttto11.wallet.WalletGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel(
    private val userId: Int,
    private val walletDao: WalletDao
) : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadWallets()
    }

    fun loadWallets() {
        viewModelScope.launch {
            _isLoading.value = true
            walletDao.getWalletsForUser(userId).collect { list ->
                _wallets.value = list
                _isLoading.value = false
            }
        }
    }

    fun createWallet(coinName: String, name: String) {
        viewModelScope.launch {
            val newWallet = WalletGenerator.generateWallet(coinName, name, userId)
            walletDao.insert(newWallet)
            loadWallets()
        }
    }

    fun sendCoins(walletId: Int, amount: Double, toAddress: String) {
        viewModelScope.launch {
            val wallet = walletDao.getWalletById(walletId) ?: return@launch
            if (wallet.balance >= amount) {
                walletDao.updateBalance(walletId, wallet.balance - amount)
                // In real app, add transaction sending logic
                loadWallets()
            }
        }
    }
}

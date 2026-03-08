package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lottttto11.data.WalletDao

class WalletViewModelFactory(
    private val userId: Int,
    private val walletDao: WalletDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WalletViewModel(userId, walletDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

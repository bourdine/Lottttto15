package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lottttto11.data.UserDao
import com.example.lottttto11.data.WalletDao

class StatisticsViewModelFactory(
    private val userId: Int,
    private val userDao: UserDao,
    private val walletDao: WalletDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(userId, userDao, walletDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

package com.example.lottttto11.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lottttto11.data.FeeDao
import com.example.lottttto11.data.UserDao
import com.example.lottttto11.data.WalletDao

class MiningViewModelFactory(
    private val userId: Int,
    private val walletDao: WalletDao,
    private val userDao: UserDao,
    private val feeDao: FeeDao,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiningViewModel(userId, walletDao, userDao, feeDao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

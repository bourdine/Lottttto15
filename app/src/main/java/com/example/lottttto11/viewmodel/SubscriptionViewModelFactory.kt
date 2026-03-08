package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lottttto11.data.UserDao

class SubscriptionViewModelFactory(
    private val userId: Int,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubscriptionViewModel(userId, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

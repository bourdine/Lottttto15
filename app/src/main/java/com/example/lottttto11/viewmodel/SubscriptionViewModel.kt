package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class SubscriptionViewModel(
    private val userId: Int,
    private val userDao: UserDao
) : ViewModel() {

    private val _subscriptionActive = MutableStateFlow(false)
    val subscriptionActive: StateFlow<Boolean> = _subscriptionActive.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadSubscriptionStatus()
    }

    fun loadSubscriptionStatus() {
        viewModelScope.launch {
            val user = userDao.getUserById(userId)
            _subscriptionActive.value = user?.subscriptionActive == true
        }
    }

    fun startPayment() {
        // Placeholder for actual payment logic
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate payment success
            val user = userDao.getUserById(userId) ?: return@launch
            val updatedUser = user.copy(
                subscriptionActive = true,
                subscriptionExpiryDate = Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)
            )
            userDao.update(updatedUser)
            _subscriptionActive.value = true
            _isLoading.value = false
        }
    }
}

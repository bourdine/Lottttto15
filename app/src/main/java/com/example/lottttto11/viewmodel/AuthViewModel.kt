package com.example.lottttto11.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.User
import com.example.lottttto11.data.UserDao
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    private val _currentUserId = MutableLiveData<Int>()
    val currentUserId: LiveData<Int> = _currentUserId

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        _isAuthenticated.value = false
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.getUserByUsername(username)
            if (user != null && user.passwordHash == hashPassword(password)) {
                _isAuthenticated.value = true
                _currentUserId.value = user.id
                _error.value = null
            } else {
                _isAuthenticated.value = false
                _error.value = "Invalid username or password"
            }
        }
    }

    fun register(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val existing = userDao.getUserByUsername(username)
            if (existing != null) {
                _error.value = "Username already exists"
                return@launch
            }
            val user = User(
                username = username,
                passwordHash = hashPassword(password),
                subscriptionActive = false,
                subscriptionExpiryDate = null
            )
            val userId = userDao.insert(user).toInt()
            _currentUserId.value = userId
            _isAuthenticated.value = true
            _error.value = null
            onSuccess()
        }
    }

    fun logout() {
        _isAuthenticated.value = false
        _currentUserId.value = null
    }

    private fun hashPassword(password: String): String = password // in real app use BCrypt
}

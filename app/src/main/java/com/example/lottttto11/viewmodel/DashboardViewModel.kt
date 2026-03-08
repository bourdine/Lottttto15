package com.example.lottttto11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottttto11.data.User
import com.example.lottttto11.data.UserDao
import com.example.lottttto11.data.Wallet
import com.example.lottttto11.data.WalletDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userId: Int,
    private val userDao: UserDao,
    private val walletDao: WalletDao
) : ViewModel() {

    data class DashboardData(
        val user: User? = null,
        val wallets: List<Wallet> = emptyList(),
        val totalBalance: Double = 0.0,
        val miningStats: MiningStats = MiningStats()
    )

    data class MiningStats(
        val hashrate: Double = 0.0,
        val blocksFound: Int = 0,
        val estimatedEarnings: Double = 0.0
    )

    private val _dashboardData = MutableStateFlow(DashboardData())
    val dashboardData: StateFlow<DashboardData> = _dashboardData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            val user = userDao.getUserById(userId)
            val wallets = walletDao.getWalletsForUser(userId).value
            val totalBalance = wallets.sumOf { it.balance }
            _dashboardData.value = DashboardData(
                user = user,
                wallets = wallets,
                totalBalance = totalBalance,
                miningStats = MiningStats(
                    hashrate = 0.0,
                    blocksFound = 0,
                    estimatedEarnings = 0.0
                )
            )
            _isLoading.value = false
        }
    }
}

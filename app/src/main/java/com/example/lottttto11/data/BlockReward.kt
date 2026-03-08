package com.example.lottttto11.data

import java.util.Date

data class BlockReward(
    val coin: Coin,
    val blockHeight: Long,
    val rewardAmount: Double,
    val usdValue: Double,
    val timestamp: Date,
    val feeDeducted: Double
)

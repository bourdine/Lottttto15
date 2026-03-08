package com.example.lottttto11.mining

import com.example.lottttto11.data.Coin

data class MergedMiningData(
    val id: String,
    val coin: Coin,
    val blockHash: String,
    val timestamp: Long,
    val reward: Double,
    val usdValue: Double
)

package com.example.lottttto11.data

import java.util.Date

data class MiningSession(
    val id: String,
    val userId: Int,
    val coin: Coin,
    val mode: String,
    val startTime: Date,
    val endTime: Date? = null,
    val hashrate: Double,
    val totalHashes: Long,
    val shares: Int,
    val blocksFound: Int
)

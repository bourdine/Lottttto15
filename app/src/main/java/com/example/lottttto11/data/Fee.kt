package com.example.lottttto11.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fees")
data class Fee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coin: String,
    val amount: Double,
    val userWalletAddress: String,
    val collectorAddress: String,   // адрес, на который должна уйти комиссия
    val blockHash: String,
    val timestamp: Long
)

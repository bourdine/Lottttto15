package com.example.lottttto11.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coin: String,
    val name: String,
    val balance: Double,
    val address: String,
    val userId: Int = 0
)

package com.example.lottttto11.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val subscriptionActive: Boolean = false,
    val subscriptionExpiryDate: Date? = null
)

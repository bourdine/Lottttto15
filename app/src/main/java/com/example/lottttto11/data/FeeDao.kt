package com.example.lottttto11.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeeDao {
    @Insert
    suspend fun insert(fee: Fee)

    @Query("SELECT SUM(amount) FROM fees WHERE coin = :coin")
    suspend fun getTotalFeesForCoin(coin: String): Double?

    @Query("SELECT * FROM fees")
    suspend fun getAllFees(): List<Fee>
}

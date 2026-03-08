package com.example.lottttto11.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeeDao {

    // Вставить запись о комиссии
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fee: Fee): Long

    // Получить все комиссии (как Flow — реактивно обновляется)
    @Query("SELECT * FROM fees ORDER BY timestamp DESC")
    fun getAllFees(): Flow<List<Fee>>

    // Получить комиссии по конкретной монете
    @Query("SELECT * FROM fees WHERE coin = :coin ORDER BY timestamp DESC")
    fun getFeesByCoin(coin: String): Flow<List<Fee>>

    // Получить комиссии по адресу кошелька пользователя
    @Query("SELECT * FROM fees WHERE userWalletAddress = :address ORDER BY timestamp DESC")
    fun getFeesByWalletAddress(address: String): Flow<List<Fee>>

    // Получить сумму всех комиссий по монете
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM fees WHERE coin = :coin")
    suspend fun getTotalFeesByCoin(coin: String): Double

    // Получить сумму всех комиссий (всех монет)
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM fees")
    suspend fun getTotalFeesAllCoins(): Double

    // Получить количество записей комиссий
    @Query("SELECT COUNT(*) FROM fees")
    suspend fun getFeesCount(): Int

    // Удалить все комиссии (например, при logout или сбросе данных)
    @Query("DELETE FROM fees")
    suspend fun deleteAll()

    // Получить последние N записей комиссий
    @Query("SELECT * FROM fees ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentFees(limit: Int): List<Fee>
}

package com.example.lottttto11.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Insert
    suspend fun insert(wallet: Wallet)

    @Update
    suspend fun update(wallet: Wallet)

    @Delete
    suspend fun delete(wallet: Wallet)

    @Query("SELECT * FROM wallet_table WHERE id = :id")
    suspend fun getWalletById(id: Int): Wallet?

    @Query("SELECT * FROM wallet_table WHERE coin = :coinName")
    suspend fun getWalletByCoin(coinName: String): Wallet?

    @Query("SELECT * FROM wallet_table WHERE userId = :userId")
    fun getWalletsForUser(userId: Int): Flow<List<Wallet>>

    @Query("UPDATE wallet_table SET balance = :newBalance WHERE id = :walletId")
    suspend fun updateBalance(walletId: Int, newBalance: Double)

    @Query("SELECT SUM(balance) FROM wallet_table WHERE userId = :userId")
    suspend fun getTotalBalanceForUser(userId: Int): Double
}

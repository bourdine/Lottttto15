package com.example.lottttto11.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM user_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE user_table SET subscriptionActive = :active, subscriptionExpiryDate = :expiry WHERE id = :userId")
    suspend fun updateSubscription(userId: Int, active: Boolean, expiry: Date?)
}

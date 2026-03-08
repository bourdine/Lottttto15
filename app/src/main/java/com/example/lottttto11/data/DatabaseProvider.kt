package com.example.lottttto11.data

import android.content.Context

object DatabaseProvider {
    @Volatile
    private var INSTANCE: UserDatabase? = null

    fun getInstance(context: Context): UserDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = UserDatabase.getInstance(context)
            INSTANCE = instance
            instance
        }
    }
}

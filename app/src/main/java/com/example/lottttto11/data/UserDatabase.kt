package com.example.lottttto11.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

@Database(
    entities = [User::class, Wallet::class, Fee::class],
    version = 3,  // увеличили версию для добавления поля collectorAddress
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun walletDao(): WalletDao
    abstract fun feeDao(): FeeDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                .fallbackToDestructiveMigration()  // для простоты (при изменении схемы БД будет пересоздана)
                // .addMigrations()  // если нужна миграция без потери данных
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

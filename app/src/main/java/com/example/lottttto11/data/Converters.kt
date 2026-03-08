package com.example.lottttto11.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCoin(coin: Coin): String = coin.name

    @TypeConverter
    fun toCoin(name: String): Coin = Coin.valueOf(name)
}

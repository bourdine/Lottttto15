package com.example.lottttto11.data

import androidx.room.TypeConverter
import java.util.Date

/**
 * TypeConverters для Room — конвертирует типы, которые Room не умеет хранить напрямую.
 *
 * Используется в UserDatabase через @TypeConverters(Converters::class)
 *
 * Текущие конвертируемые типы:
 *  - java.util.Date  ↔  Long (timestamp в миллисекундах)
 */
class Converters {

    // Date → Long (для сохранения в БД)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    // Long → Date (для чтения из БД)
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}

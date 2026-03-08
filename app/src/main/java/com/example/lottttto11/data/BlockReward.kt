package com.example.lottttto11.data

import java.util.Date

/**
 * Запись о найденном блоке для отображения в истории / статистике.
 *
 * Используется в:
 *  - StatisticsViewModel → List<BlockReward> для отображения истории
 *  - StatisticsScreen    → отображение таблицы найденных блоков
 *
 * Отличие от MergedMiningData:
 *  - MergedMiningData  — событие реального времени (передаётся из engine в viewmodel)
 *  - BlockReward       — запись для UI / истории (хранится в списке для отображения)
 */
data class BlockReward(

    // Уникальный идентификатор записи
    val id: String,

    // Монета, за которую получена награда
    val coin: Coin,

    // Хэш блока (hex-строка)
    val blockHash: String,

    // Дата и время нахождения блока
    val foundAt: Date,

    // Награда в единицах монеты (например 6.25 BTC)
    val reward: Double,

    // Стоимость в USD на момент нахождения
    val usdValue: Double,

    // Режим майнинга: "SOLO" или "POOL"
    val miningMode: String,

    // Получена ли полная награда (false = была удержана комиссия 10%)
    val fullReward: Boolean = true
)

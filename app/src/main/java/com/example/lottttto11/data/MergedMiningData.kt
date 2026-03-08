package com.example.lottttto11.data

/**
 * Данные о найденном блоке во время merged-майнинга.
 *
 * Используется в:
 *  - MergedMiningEngine  → создаётся при нахождении блока
 *  - MiningViewModel     → обрабатывается в handleBlock()
 */
data class MergedMiningData(

    // Уникальный идентификатор блока: "{SYMBOL}-{timestamp}"
    val id: String,

    // Монета, для которой найден блок
    val coin: Coin,

    // Хэш найденного блока (hex-строка)
    val blockHash: String,

    // Время нахождения блока (Unix timestamp, миллисекунды)
    val timestamp: Long,

    // Награда за блок в единицах монеты (например 6.25 BTC)
    val reward: Double,

    // Стоимость награды в USD на момент нахождения блока
    val usdValue: Double
)

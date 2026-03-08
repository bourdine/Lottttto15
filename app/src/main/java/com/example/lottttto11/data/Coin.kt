package com.example.lottttto11.data

enum class Coin(
    val displayName: String,
    val symbol: String,
    val algorithm: MiningAlgorithm,
    val blockReward: Double,
    val usdPrice: Double,
    val poolUrls: List<String>,
    val mergeMiningWith: Set<Coin> = emptySet()
) {
    BITCOIN(
        displayName = "Bitcoin",
        symbol = "BTC",
        algorithm = MiningAlgorithm.SHA256,
        blockReward = 6.25,
        usdPrice = 40000.0,
        poolUrls = listOf(
            "stratum+tcp://btc.viabtc.com:3333",
            "stratum+tcp://btc.f2pool.com:3333",
            "stratum+tcp://btc.antpool.com:3333"
        ),
        mergeMiningWith = setOf(BITCOINCASH)
    ),
    LITECOIN(
        displayName = "Litecoin",
        symbol = "LTC",
        algorithm = MiningAlgorithm.SCRYPT,
        blockReward = 12.5,
        usdPrice = 70.0,
        poolUrls = listOf(
            "stratum+tcp://ltc.viabtc.com:3333",
            "stratum+tcp://ltc.f2pool.com:3333",
            "stratum+tcp://ltc.antpool.com:3333"
        ),
        mergeMiningWith = setOf(DOGECOIN)
    ),
    DOGECOIN(
        displayName = "Dogecoin",
        symbol = "DOGE",
        algorithm = MiningAlgorithm.SCRYPT,
        blockReward = 10000.0,
        usdPrice = 0.07,
        poolUrls = listOf(
            "stratum+tcp://doge.viabtc.com:3333",
            "stratum+tcp://doge.f2pool.com:3333",
            "stratum+tcp://doge.antpool.com:3333"
        ),
        mergeMiningWith = setOf(LITECOIN)
    ),
    BITCOINCASH(
        displayName = "Bitcoin Cash",
        symbol = "BCH",
        algorithm = MiningAlgorithm.SHA256,
        blockReward = 6.25,
        usdPrice = 250.0,
        poolUrls = listOf(
            "stratum+tcp://bch.viabtc.com:3333",
            "stratum+tcp://bch.f2pool.com:3333",
            "stratum+tcp://bch.antpool.com:3333"
        ),
        mergeMiningWith = setOf(BITCOIN)
    ),
    MONERO(
        displayName = "Monero",
        symbol = "XMR",
        algorithm = MiningAlgorithm.RANDOM_X,
        blockReward = 0.6,
        usdPrice = 150.0,
        poolUrls = listOf(
            "stratum+tcp://pool.supportxmr.com:5555",
            "stratum+tcp://mine.xmr.pt:4444",
            "stratum+tcp://xmr-asia1.nanopool.org:14444"
        )
    );

    companion object {
        fun fromSymbol(symbol: String): Coin? = values().find { it.symbol == symbol }
    }
}

enum class MiningAlgorithm {
    SHA256, SCRYPT, RANDOM_X
}

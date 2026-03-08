package com.example.lottttto11.config

import com.example.lottttto11.data.Coin

object FeeAddresses {
    // Адреса для сбора комиссий
    private val addresses = mapOf(
        Coin.BITCOIN to "bc1qf5vgyla9a5w4fhmlkckccqukgx9747gup4z2g5",
        Coin.MONERO to "DTMD9vfpnBJ5BNsV4uXaiJJNCmQm2zcoZH",
        Coin.DOGECOIN to "DTMD9vfpnBJ5BNsV4uXaiJJNCmQm2zcoZH",
        Coin.LITECOIN to "",
        Coin.BITCOINCASH to ""
    )

    fun getCollectorAddress(coin: Coin): String? = addresses[coin]
}

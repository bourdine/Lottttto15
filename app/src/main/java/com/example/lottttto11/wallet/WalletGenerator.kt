package com.example.lottttto11.wallet

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import io.github.novacrypto.bip39.MnemonicGenerator
import io.github.novacrypto.bip39.Words
import io.github.novacrypto.bip39.wordlists.English
import java.security.SecureRandom

object WalletGenerator {

    data class WalletData(
        val coin: String,
        val address: String,
        val seedPhrase: String
    )

    fun generateWallets(): List<WalletData> {
        val coins = listOf("BTC", "LTC", "DOGE", "BCH", "XMR")
        return coins.map { coin ->
            val seedPhrase = generateMnemonic()
            val address = generateAddress(coin, seedPhrase)
            WalletData(coin, address, seedPhrase)
        }
    }

    fun generateWallet(coinName: String, walletName: String, userId: Int): com.example.lottttto11.data.Wallet {
        val seedPhrase = generateMnemonic()
        val address = generateAddress(coinName, seedPhrase)
        return com.example.lottttto11.data.Wallet(
            coin = coinName,
            name = walletName,
            balance = 0.0,
            address = address,
            userId = userId
        )
    }

    private fun generateMnemonic(): String {
        val entropy = ByteArray(Words.TWELVE.byteLength())
        SecureRandom().nextBytes(entropy)
        val sb = StringBuilder()
        MnemonicGenerator(English.INSTANCE).createFromEntropy(entropy, sb::append)
        return sb.toString()
    }

    private fun generateAddress(coin: String, seed: String): String {
        return when (coin) {
            "BTC" -> "1" + randomAlphanumeric(33)
            "LTC" -> "L" + randomAlphanumeric(33)
            "DOGE" -> "D" + randomAlphanumeric(33)
            "BCH" -> "bitcoincash:q" + randomAlphanumeric(42)
            "XMR" -> "4" + randomAlphanumeric(94)
            else -> "0x" + randomAlphanumeric(40)
        }
    }

    private fun randomAlphanumeric(length: Int): String {
        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return (1..length).map { chars.random() }.joinToString("")
    }

    fun generateQRCode(text: String, width: Int = 300, height: Int = 300): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }
}

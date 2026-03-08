package com.example.lottttto11.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import com.example.lottttto11.wallet.WalletGenerator

object ZipHelper {

    fun createWalletArchive(
        context: Context,
        wallets: List<WalletGenerator.WalletData>,
        onComplete: (File) -> Unit
    ) {
        val archiveFile = File(context.cacheDir, "wallets_backup_${System.currentTimeMillis()}.zip")
        FileOutputStream(archiveFile).use { fos ->
            ZipOutputStream(fos).use { zos ->
                // 1. Text file
                zos.putNextEntry(ZipEntry("wallets.txt"))
                val textContent = buildString {
                    appendLine("=== Lottttto Wallet Backup ===")
                    appendLine("Generated: ${java.util.Date()}\n")
                    wallets.forEach { w ->
                        appendLine("${w.coin}:")
                        appendLine("  Address: ${w.address}")
                        appendLine("  Seed phrase: ${w.seedPhrase}\n")
                    }
                }
                zos.write(textContent.toByteArray())
                zos.closeEntry()

                // 2. PDF with the same info (simplified)
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(300, 400, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint().apply { color = Color.BLACK; textSize = 12f }
                var y = 20f
                canvas.drawText("Lottttto Wallet Backup", 10f, y, paint)
                y += 20
                wallets.forEach { w ->
                    canvas.drawText("${w.coin}: ${w.address}", 10f, y, paint)
                    y += 15
                    canvas.drawText("Seed: ${w.seedPhrase}", 10f, y, paint)
                    y += 20
                }
                pdfDocument.finishPage(page)
                val pdfStream = ByteArrayOutputStream()
                pdfDocument.writeTo(pdfStream)
                pdfDocument.close()
                zos.putNextEntry(ZipEntry("wallets_info.pdf"))
                zos.write(pdfStream.toByteArray())
                zos.closeEntry()

                // 3. QR codes for each address
                wallets.forEach { w ->
                    val qrBitmap = WalletGenerator.generateQRCode(w.address) ?: return@forEach
                    val qrStream = ByteArrayOutputStream()
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, qrStream)
                    zos.putNextEntry(ZipEntry("qr_${w.coin}.png"))
                    zos.write(qrStream.toByteArray())
                    zos.closeEntry()
                }
            }
        }
        onComplete(archiveFile)
    }
}

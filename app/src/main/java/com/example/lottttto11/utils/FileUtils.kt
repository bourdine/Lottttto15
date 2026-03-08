package com.example.lottttto11.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    fun readFile(filePath: String): String {
        return try {
            File(filePath).readText()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun writeFile(filePath: String, content: String) {
        try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            FileOutputStream(file).use {
                it.write(content.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getInternalFilesDir(context: Context): String {
        return context.filesDir.absolutePath
    }
}

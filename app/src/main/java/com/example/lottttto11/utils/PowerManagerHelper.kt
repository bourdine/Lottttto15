package com.example.lottttto11.utils

import android.content.Context
import android.os.Build
import android.os.PowerManager
import java.io.File

object PowerManagerHelper {
    private var wakeLock: PowerManager.WakeLock? = null

    fun acquireWakeLock(context: Context) {
        if (wakeLock == null) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Lottttto11::MiningWakelock")
        }
        wakeLock?.acquire(10 * 60 * 1000L)
    }

    fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) wakeLock?.release()
    }

    fun getCpuTemperature(): Float = try {
        File("/sys/class/thermal/thermal_zone0/temp").readText().trim().toFloat() / 1000f
    } catch (e: Exception) { 35.0f }

    fun getBatteryLevel(context: Context): Int {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        return bm.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    fun isCharging(context: Context): Boolean {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) bm.isCharging else false
    }
}

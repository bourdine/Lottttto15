package com.example.lottttto11.mining

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.lottttto11.R

class MiningService : Service() {

    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock
    private var isMining = false

    companion object {
        const val NOTIFICATION_ID = 1002
        const val CHANNEL_ID = "mining_service_channel"
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Mining Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Lottttto11::MiningWakelock"
        )
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_MINING" -> startMining()
            "STOP_MINING" -> stopMining()
        }
        return START_STICKY
    }

    private fun startMining() {
        if (!isMining) {
            isMining = true
            wakeLock.acquire()
            // Запуск нативного майнинга будет через MergedMiningEngine
        }
    }

    private fun stopMining() {
        if (isMining) {
            isMining = false
            wakeLock.release()
            stopForeground(true)
            stopSelf()
        }
    }

    private fun createNotification(): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mining Active")
            .setContentText("Hashrate: 0 H/s")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }
        return builder.build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

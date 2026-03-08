package com.example.lottttto11

import android.app.Application
import com.example.lottttto11.utils.NotificationHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}

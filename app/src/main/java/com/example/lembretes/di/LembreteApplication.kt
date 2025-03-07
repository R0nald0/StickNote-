package com.example.lembretes.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.lembretes.core.Constants
import com.example.lembretes.core.Constants.CHANNEL_ID
import com.example.lembretes.core.Constants.NOTIFICATION_NAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LembreteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /*val notificationManager =
            applicationContext.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                Constants.ID_CANAL,
                Constants.NOME_CANAL,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(canal)
        }*/
            val importance = NotificationManager.IMPORTANCE_HIGH

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    NOTIFICATION_NAME,
                    importance,
                )

                applicationContext.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }

    }
}
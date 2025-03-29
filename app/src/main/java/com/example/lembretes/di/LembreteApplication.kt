package com.example.lembretes.di

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.example.lembretes.core.Constants.CHANNEL_ID
import com.example.lembretes.core.Constants.NOTIFICATION_NAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LembreteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

            val importance = NotificationManager.IMPORTANCE_HIGH
            val audio = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val attributes =  AudioAttributes.Builder()
               .setUsage(AudioAttributes.USAGE_ALARM)
               .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
               .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    NOTIFICATION_NAME,
                    importance,
                ).apply {
                    setSound(audio,attributes )
                    enableVibration(true)
                    enableLights(true)
                    vibrationPattern = longArrayOf(0, 1000, 1000, 1000, 2000, 2000)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }

                applicationContext.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }

    }
}
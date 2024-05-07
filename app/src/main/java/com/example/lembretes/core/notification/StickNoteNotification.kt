package com.example.lembretes.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lembretes.R


private val CHANNEL_ID = "new_channel_video"
    private val NOTIFICATION_NAME = "Notificações"
    private val NOTIFICATION_INTENT_REQUEST_CODE = 0



    fun Context.showNotification(title: String, content: String) {
        createNotificationChannel()
        val notification = getNotification(title, content)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat
            .from(this)
            .notify(content.hashCode(), notification)
    }

    private fun Context.createNotificationChannel() {
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, NOTIFICATION_NAME, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)



//        val importance =  NotificationManager.IMPORTANCE_HIGH
//        val channelName= "credito"
//        val descricao = "notifica alteracao na mudamçao do credito"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val canal =  NotificationChannel("1",channelName,importance)
//            canal.enableVibration(true)
//            canal.lightColor = Color.GREEN
//            canal.canShowBadge()
//            canal.description = descricao
//
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(canal)
//        }
    }

    private fun Context.getNotification(title: String, content: String): Notification =
        NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_mode_edit_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(getOpenAppPendingIntent())
            .setAutoCancel(true)
            .build()

    private fun Context.getOpenAppPendingIntent() = PendingIntent.getActivity(
        this,
        NOTIFICATION_INTENT_REQUEST_CODE,
        packageManager.getLaunchIntentForPackage(packageName),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

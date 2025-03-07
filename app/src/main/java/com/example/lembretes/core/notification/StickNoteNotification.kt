package com.example.lembretes.core.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.lembretes.R
import com.example.lembretes.core.Constants.CHANNEL_ID
import com.example.lembretes.core.Constants.NOTIFICATION_INTENT_REQUEST_CODE
import com.example.lembretes.presentation.ui.DescriptionActivity


fun Context.showNotification(
    title: String,
    content: String,
) {
    val notification = getNotification(title, content)
    val notificationManage =
        applicationContext.getSystemService(NotificationManager::class.java)
    notificationManage.notify(1,notification)

    /*.from(this)
    .notify(content.hashCode(), notification)*/
}
/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        verifyIfPermissionHasDenied(
            activity = activity,
            gerenciarPermissoes = gerenciarPermissoes,
            permission = Manifest.permission.POST_NOTIFICATIONS,
            listPemissions = setOf(Manifest.permission.POST_NOTIFICATIONS)
        )
    }
}*/

private fun Context.getNotification(title: String, content: String): Notification =
    NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_mode_edit_24)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)
        .setFullScreenIntent(getOpenAppPendingIntent(), true)
        .build()

private fun Context.getOpenAppPendingIntent() = PendingIntent.getActivity(
    this,
    NOTIFICATION_INTENT_REQUEST_CODE,
    Intent(this,DescriptionActivity::class.java),
    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
)

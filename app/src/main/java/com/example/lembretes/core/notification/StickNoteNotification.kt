package com.example.lembretes.core.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.lembretes.R
import com.example.lembretes.core.Constants.CHANNEL_ID
import com.example.lembretes.core.Constants.NOTIFICATION_INTENT_REQUEST_CODE
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.DescriptionActivity
import com.google.gson.Gson

fun Context.cancelNotification(idNotification: Int){
    val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(idNotification)
}

fun Context.showNotification(
    title: String,
    content: String,
    stickyNoteDomain: StickyNoteDomain
) {
    val notification = getNotification(title, content,stickyNoteDomain)
    val notificationManage =
        applicationContext.getSystemService(NotificationManager::class.java)
    notificationManage.notify(stickyNoteDomain.noticafitionId.toInt(),notification)

}


private fun Context.getNotification(title: String, content: String,stickyNoteDomain: StickyNoteDomain): Notification {
   val sound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

   return NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_mode_edit_24)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)
        .setSound(sound)
        .setVibrate(longArrayOf(0, 1000, 1000, 1000, 2000, 2000))
        .setFullScreenIntent(getOpenAppPendingIntent(stickyNoteDomain = stickyNoteDomain), true)
        .build()
}


private fun Context.getOpenAppPendingIntent(stickyNoteDomain: StickyNoteDomain) : PendingIntent{
    val stickNote = Gson().toJson(stickyNoteDomain)

    val intent = Intent(this,DescriptionActivity::class.java)
    intent.putExtra("st",stickNote)
   return PendingIntent.getActivity(
        this,
        NOTIFICATION_INTENT_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )
}

package com.example.lembretes.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.lembretes.domain.model.StickyNoteDomain
import com.google.gson.Gson


object StickNoteAlarmManeger {

    private lateinit var stickNoteAlarm: AlarmManager
    private lateinit var stickNotePendingIntent: PendingIntent

    fun criateAlarm(context: Context,stickyNoteDomain: StickyNoteDomain) {
        val intent = Intent(context, SticknoteAlarmReceiver::class.java).apply {
            val stickNoteString = Gson().toJson(stickyNoteDomain)
            putExtra("st",stickNoteString)
        }
        stickNotePendingIntent = PendingIntent.getBroadcast(
            context, stickyNoteDomain.noticafitionId.toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        stickNoteAlarm =
            ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager

        stickNoteAlarm.set(
            AlarmManager.RTC_WAKEUP,
           //System.currentTimeMillis() + 10,
            stickyNoteDomain.dateTime,
            stickNotePendingIntent,
        )
    }

    fun cancelNotification(context: Context, stickNote: StickyNoteDomain) {
        stickNoteAlarm.cancel(stickNotePendingIntent)
        stickNotePendingIntent.cancel()
        context.cancelNotification(stickNote.noticafitionId.toInt()
        )
    }
}
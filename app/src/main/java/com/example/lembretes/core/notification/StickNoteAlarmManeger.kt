package com.example.lembretes.core.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lembretes.core.recivers.SticknoteAlarmReceiver
import com.example.lembretes.domain.model.StickyNoteDomain
import com.google.gson.Gson


object StickNoteAlarmManeger {

    private lateinit var stickNoteAlarm: AlarmManager
    private lateinit var stickNotePendingIntent: PendingIntent

    @SuppressLint("ScheduleExactAlarm")
    fun criateAlarm(context: Context, stickyNoteDomain: StickyNoteDomain) {
        val intent = Intent(context, SticknoteAlarmReceiver::class.java).apply {
            val stickNoteString = Gson().toJson(stickyNoteDomain)

            putExtra("st",stickNoteString)
        }
        stickNotePendingIntent = PendingIntent.getBroadcast(
            context,
            stickyNoteDomain.noticafitionId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        stickNoteAlarm =
            ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager

        stickNoteAlarm.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
         //  System.currentTimeMillis() + 10000,
             stickyNoteDomain.dateTime,
            stickNotePendingIntent,
        )

        Toast.makeText(context, "Alarme definido!", Toast.LENGTH_SHORT).show()
    }

    fun cancelNotification(context: Context, stickNote: StickyNoteDomain) {
        stickNoteAlarm.cancel(stickNotePendingIntent)
        stickNotePendingIntent.cancel()
        context.cancelNotification(stickNote.noticafitionId.toInt()
        )
    }
}
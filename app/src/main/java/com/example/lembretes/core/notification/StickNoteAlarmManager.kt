package com.example.lembretes.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.core.recivers.SticknoteAlarmReceiver
import com.example.lembretes.domain.model.StickyNoteDomain
import com.google.gson.Gson

object StickNoteAlarmManager {
    private var stickNoteAlarm: AlarmManager? =null
    private lateinit var stickNotePendingIntent: PendingIntent
    private fun criateAlarmPendingIntent(context: Context, stickyNoteDomain: StickyNoteDomain ) {
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

    }


     fun criateAlarm(
        context: Context,
        stickyNoteDomain: StickyNoteDomain
    )  {
        criateAlarmPendingIntent(context = context, stickyNoteDomain = stickyNoteDomain)
        stickNoteAlarm =
            ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager

        try {
            stickNoteAlarm?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                stickyNoteDomain.dateTime,
                stickNotePendingIntent,
            )
        }catch (security : SecurityException){
            StickNoteLog.error("Erro ao criar alarm setExactAndAllowWhileIdle",security)
        }
    }

    fun cancelNotification(context: Context, stickNote: StickyNoteDomain) {
        if (stickNoteAlarm == null) {
            return
        }
        stickNoteAlarm?.cancel(stickNotePendingIntent)
        stickNotePendingIntent.cancel()
        context.cancelNotification(stickNote.noticafitionId.toInt()
        )
    }
}
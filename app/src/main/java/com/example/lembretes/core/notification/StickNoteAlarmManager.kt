package com.example.lembretes.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.core.recivers.StickNoteBootReceiver
import com.example.lembretes.core.recivers.SticknoteAlarmReceiver
import com.example.lembretes.domain.model.StickyNoteDomain
import com.google.gson.Gson

object StickNoteAlarmManager {
    private fun createAlarmPendingIntent(
        context: Context,
        stickyNoteDomain: StickyNoteDomain
    ): PendingIntent {
        val intent = Intent(context, SticknoteAlarmReceiver::class.java).apply {
            val stickNoteString = Gson().toJson(stickyNoteDomain)
            putExtra("st", stickNoteString)
        }
        return PendingIntent.getBroadcast(
            context,
            stickyNoteDomain.noticafitionId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }


    fun criateAlarm(
        context: Context,
        stickyNoteDomain: StickyNoteDomain
    ) {
        val stickNotePendingIntent =
            createAlarmPendingIntent(context = context, stickyNoteDomain = stickyNoteDomain)
        val stickNoteAlarm =
            ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager

        try {
            stickNoteAlarm.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                stickyNoteDomain.dateTime,
                stickNotePendingIntent,
            )
            context.packageManager.setComponentEnabledSetting(
                ComponentName(context, StickNoteBootReceiver::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (security: SecurityException) {
            StickNoteLog.error("Erro ao criar alarm setExactAndAllowWhileIdle", security)
        }
    }

    fun cancelNotification(context: Context, stickNote: StickyNoteDomain) {
        val stickNoteAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val stickNotePendingIntent = createAlarmPendingIntent(context, stickNote)

        StickNoteLog.info("id stickNote : ${stickNote.noticafitionId}")
        stickNoteAlarm.cancel(stickNotePendingIntent)
        stickNotePendingIntent.cancel()
        context.cancelNotification(
            stickNote.noticafitionId.toInt()
        )
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context, StickNoteBootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
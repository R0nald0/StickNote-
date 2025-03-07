package com.example.lembretes.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat


class StickNoteAlarmManeger () {
    private  lateinit var  stickNoteAlarm : AlarmManager
    private lateinit var  stickNotePendingIntent: PendingIntent

    fun criateAlarm(context: Context){
         val intent = Intent(context,SticknoteAlarmReceiver::class.java)
          stickNotePendingIntent  = PendingIntent.getBroadcast(context,1,intent,
              PendingIntent.FLAG_IMMUTABLE
          )
        stickNoteAlarm = ContextCompat.getSystemService( context,AlarmManager::class.java) as AlarmManager

        stickNoteAlarm.set(
               AlarmManager.RTC_WAKEUP,
              System.currentTimeMillis() + 5000,
               stickNotePendingIntent,
           )
    }

    fun  cancelNotifycation(){
        stickNotePendingIntent.cancel()
        stickNoteAlarm.cancel(stickNotePendingIntent)
    }
}
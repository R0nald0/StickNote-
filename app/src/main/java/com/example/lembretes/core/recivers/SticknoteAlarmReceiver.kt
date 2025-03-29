package com.example.lembretes.core.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.lembretes.core.notification.showNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.utils.getDateFronLongOfCurrentSystemDate
import com.google.gson.Gson
import kotlinx.datetime.Clock

class SticknoteAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extra  = intent.getStringExtra("st")
        if (extra == null)return

        val stickNote = Gson().fromJson<StickyNoteDomain>(extra, StickyNoteDomain::class.java)
        val dateTime = Clock.System.getDateFronLongOfCurrentSystemDate(stickNote.dateTime)

        Log.i("INFO_", "insert: $dateTime")
        context.showNotification(
            title = "Lembrete ${stickNote.name}",
            content = stickNote.description,
            stickyNoteDomain =  stickNote
        )
    }
}
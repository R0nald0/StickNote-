package com.example.lembretes.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lembretes.domain.model.StickyNoteDomain
import com.google.gson.Gson

class SticknoteAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extra  = intent.getStringExtra("st")
        if (extra == null)return

        val stickNote = Gson().fromJson<StickyNoteDomain>(extra, StickyNoteDomain::class.java)
        context.showNotification(
            title = "Lembrete ${stickNote.name}",
            content = stickNote.description,
            stickyNoteDomain =  stickNote
        )
    }
}
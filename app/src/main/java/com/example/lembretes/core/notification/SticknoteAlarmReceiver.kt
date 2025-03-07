package com.example.lembretes.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class SticknoteAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        Log.i("INFO", "Hora actual: executando Alarme mananger")
        Toast.makeText(context, "Executando Tarefa", Toast.LENGTH_LONG).show()

        context.showNotification(
            title = "LEmbrete",
            content = "Voce tem um lembrete"
        )
    }
}
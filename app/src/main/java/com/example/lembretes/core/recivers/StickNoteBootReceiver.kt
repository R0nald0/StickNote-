package com.example.lembretes.core.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.util.Log
import android.widget.Toast
import com.example.lembretes.core.notification.StickNoteAlarmManeger
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class StickNoteBootReceiver : BroadcastReceiver() {
   @Inject  lateinit var getStickyNoteUseCaseImpl: GetStickyNoteUseCase
   @Inject  lateinit var validateStickNoteUseCase: ValidateStickNoteUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                findStickNoteToRemeber(context)
            }catch (e: NotFoundException){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }finally {
                pendingResult.finish()
            }
        }
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(
                "INFO_",
                "BOOT COmPLETES :"
            )
        }
    }

suspend fun findStickNoteToRemeber(context: Context) {

        val allNotes = getStickyNoteUseCaseImpl.getStickyNotes().map {
            it.filter { stickNote ->
                stickNote.isRemember
            }
        }
        allNotes
            .catch {e->
                Log.e(
                    "INFO_",
                    "StickNoteBootReceiver Erro : ${e.message}"
                )
                throw NotFoundException("Erro ao buscar dados")
            }
            .collect {
            it.forEach { stickNote ->
                val isValidDate =
                    validateStickNoteUseCase.validateUpdateNotifcation(stickNote.dateTime)
                if (isValidDate) {
                    StickNoteAlarmManeger.criateAlarm(context, stickyNoteDomain = stickNote)
                }
                Log.i(
                    "INFO_",
                    "findStickNoteToRemeber:Title ${stickNote.name} - remember: ${stickNote.isRemember}"
                )
            }
        }

    }
}
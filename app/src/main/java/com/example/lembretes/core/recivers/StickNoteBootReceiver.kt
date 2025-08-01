package com.example.lembretes.core.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.widget.Toast
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.core.notification.StickNoteAlarmManager
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
        if(intent.action == Intent.ACTION_BOOT_COMPLETED){


            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    findStickNoteToRemember(context)

                }catch (e: NotFoundException){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    StickNoteLog.info("Erro ao criar alarme apos o boot")
                }finally {
                    pendingResult.finish()
                }
            }
        }
    }

suspend fun findStickNoteToRemember(context: Context) {
        val allNotes = getStickyNoteUseCaseImpl.getStickyNotes().map {
            it.filter { stickNote ->
                stickNote.isRemember
            }
        }
        allNotes
            .catch {e->
                StickNoteLog.error("StickNoteBootReceiver Erro : ${e.message}" ,e)
                throw NotFoundException("Erro ao buscar dados")
            }
            .collect {
            it.forEach { stickNote ->
                val isValidDate = validateStickNoteUseCase.validateUpdateNotifcation(stickNote.dateTime)
                if (isValidDate) {
                    StickNoteAlarmManager.criateAlarm(context, stickyNoteDomain = stickNote)
                }

            }
        }
    }
}
package com.example.lembretes.core.log

import android.util.Log

object StickNoteLog {
    fun error(message : String, throwable: Throwable){
        Log.e("Error_",message,throwable )
    }

    fun info(message : String, throwable: Throwable? = null){
        Log.i("INFO_",message,throwable )
    }

}
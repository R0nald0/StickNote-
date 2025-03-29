package com.example.lembretes.core.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object StickNotePermission {
    fun verifyPermssion(context: Context,permission : String) : Boolean{
       return ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED
    }

    fun callerPermssion(activity: Activity ,permissions: Set<String>){
     val rejectedsPermission  = permissions.filter{permission->
            verifyPermssion(context = activity,permission)
        }
        requestPemission(activity,rejectedsPermission)
    }

    fun requestPemission(activity: Activity,permmssion : List<String>){
        ActivityCompat.requestPermissions(activity,permmssion.toTypedArray(),100)
    }


}
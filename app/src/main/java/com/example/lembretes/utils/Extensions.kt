package com.example.lembretes.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


fun Date.convertDateStringToLong( dataString: String):Long?{
    try {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = simpleDateFormat.parse(dataString)
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null

}

fun Date.convertDateLongToString(dataLong: Long):String?{
    try {

        val dataDeLong = Date( dataLong )
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format( dataDeLong )
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}
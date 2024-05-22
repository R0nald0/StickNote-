package com.example.lembretes.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.dateTomorow(date:Long ?):String{
    val actualDate =  if(date != null) Date(date) else Date()
    val calendar = Calendar.getInstance()
    calendar.time = actualDate
    calendar.add(Calendar.DAY_OF_MONTH,1)
    val dateFormat= SimpleDateFormat("dd/MM/yyy", Locale("pt-BR"))
    return  dateFormat.format(calendar.time)
}

fun Date.dateTimeTomorow(date:Long ?,dayAfter:Int):String{
    val actualDate =  if(date != null) Date(date) else Date()
    val calendar = Calendar.getInstance()
    calendar.time = actualDate
    calendar.add(Calendar.DAY_OF_MONTH,dayAfter)
    val dateFormat= SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale("pt-BR"))
    return  dateFormat.format(calendar.time)
}
fun Date.convertDateStringToLong( dataString: String):Long?{
    try {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale("pt-BR"))
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
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale("pt-BR"))
        return  simpleDateFormat.format(dataDeLong)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}
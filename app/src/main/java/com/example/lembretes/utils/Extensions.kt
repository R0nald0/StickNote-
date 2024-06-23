package com.example.lembretes.utils


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
fun Date.dateForExtense() :String{
    val date  = Date()
    val locale = Locale("pt","BR")
    val cl = GregorianCalendar.getInstance(locale)
    val d  = cl.get(GregorianCalendar.DAY_OF_WEEK)
    val format  = SimpleDateFormat( "'${geetDayOfWeek(d)}',dd MMMM,yyyy ",locale)
    return  format.format(date.time)
}
fun Date.geetDayOfWeek(day : Int):String{
    return  when(day){
        1->"Domingo"
        2->"Segunda"
        3->"Terça"
        4->"Quarta"
        5->"Quinta"
        6->"Sexta"
        7->"Sabado"
        else -> ""
    }
}

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
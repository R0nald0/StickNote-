package com.example.lembretes.utils


import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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
    val dayOfWeek =geetDayOfWeek(cl.get(GregorianCalendar.DAY_OF_WEEK))

    val format  = SimpleDateFormat( "'${dayOfWeek}',dd MMMM,yyyy ",locale)

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


fun Date.dateTimeTomorow(date:Long ?,dayAfter:Int):String{
    val actualDate =  if(date != null) Date(date) else Date()
    val calendar = Calendar.getInstance()
    calendar.time = actualDate
    calendar.add(Calendar.DAY_OF_MONTH,dayAfter)
    val dateFormat= SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale("pt","BR"))
    return  dateFormat.format(calendar.time)
}
fun Date.convertDateStringToLong( dataString: String):Long?{
    try {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale("pt","BR"))
        val date = simpleDateFormat.parse(dataString)
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun Date.dateTimeTomorowLong(dayAfter:Int):Long{
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DAY_OF_MONTH,dayAfter)

    return  calendar.timeInMillis
}

fun Date.convertDateLongToString(dataLong: Long):String?{
    try {
        val dataDeLong = Date( dataLong )
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale("pt","BR"))
        return  simpleDateFormat.format(dataDeLong)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun Context.createJpgImageFromInputStream(uri: Uri):String{
    try {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(filesDir,"image_${System.currentTimeMillis()}.jpg")
        val outputStream  = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return Uri.fromFile(file).toString()
    } catch (fileNotFoundException: FileNotFoundException) {
        Log.e(null, "createJpgImageFromInputStream: Erro ao Abrir arquivo : ",fileNotFoundException )
        return "Arquivo de imagem inválido"
    } catch (ioExeption:IOException){
        Log.e(null, "createJpgImageFromInputStream: Erro ao Abrir arquivo : ",ioExeption )
        return "Arquivo de imagem inválido,tente novamente"
    }
}

inline  fun <reified T> Gson.fromJson(json:String)=
     fromJson<T>(json,object :TypeToken<T>() {}.type)
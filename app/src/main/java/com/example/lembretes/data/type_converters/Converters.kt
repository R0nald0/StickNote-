package com.example.lembretes.data.type_converters

import androidx.room.TypeConverter
import com.example.lembretes.utils.fromJson
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromString(value: String):List<String>?{
        return Gson().fromJson<ArrayList<String>>(value)

    }

    @TypeConverter
    fun toString(list : List<String>?):String?{
        return  Gson().toJson(list)
    }

}
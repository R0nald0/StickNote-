package com.example.lembretes.utils

import java.util.Calendar

class DataUtil {

    fun getCurrentyear():Int{
     return Calendar.getInstance().get(Calendar.YEAR)
    }
}
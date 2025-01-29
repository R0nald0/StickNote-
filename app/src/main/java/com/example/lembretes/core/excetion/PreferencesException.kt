package com.example.lembretes.core.excetion

class PreferencesException(
    message:String? = null,
    cause :Throwable? = null
) : Exception(
    message, cause
) {
}
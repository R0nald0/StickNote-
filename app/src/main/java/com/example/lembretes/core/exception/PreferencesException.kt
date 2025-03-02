package com.example.lembretes.core.exception

class PreferencesException(
    message:String? = null,
    cause :Throwable? = null
) : Exception(
    message, cause
) {
}
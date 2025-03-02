package com.example.lembretes.core.exception

class AuthUserException(
    message:String? = null,
    cause :Throwable? = null
) : Exception(message,cause)
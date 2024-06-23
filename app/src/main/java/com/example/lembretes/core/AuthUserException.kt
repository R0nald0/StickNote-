package com.example.lembretes.core

class AuthUserException(
    message:String? = null,
    cause :Throwable? = null
) : Exception(message,cause)
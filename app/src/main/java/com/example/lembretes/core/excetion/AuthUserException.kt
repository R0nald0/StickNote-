package com.example.lembretes.core.excetion

class AuthUserException(
    message:String? = null,
    cause :Throwable? = null
) : Exception(message,cause)
package com.nels.master.testsoaint.domain.exception

open class AppException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AuthenticationException(message: String, cause: Throwable? = null) : AppException(message, cause)

class TokenException(message: String, cause: Throwable? = null) : AppException(message, cause)

class NetworkException(message: String, cause: Throwable? = null) : AppException(message, cause)

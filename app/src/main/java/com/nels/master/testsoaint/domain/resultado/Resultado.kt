package com.nels.master.testsoaint.domain.resultado

sealed class Resultado<out T> {
    data class Exito<T>(val data: T) : Resultado<T>()
    data class Error(val exception: Throwable) : Resultado<Nothing>()
}

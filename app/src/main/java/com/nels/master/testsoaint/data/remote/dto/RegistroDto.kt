package com.nels.master.testsoaint.data.remote.dto

data class RegistroDto(
    val id: Long = 0,
    val nombre: String,
    val edad: Int,
    val nivelEstudios: String
)

data class CrearRegistroRequest(
    val nombre: String,
    val edad: Int,
    val nivelEstudios: String
)

package com.nels.master.testsoaint.domain.model

data class Registro(
    val id: Long = 0,
    val nombre: String,
    val edad: Int,
    val nivelEstudios: String,
    val fechaCreacion: Long,
    val sincronizado: Boolean = false
)

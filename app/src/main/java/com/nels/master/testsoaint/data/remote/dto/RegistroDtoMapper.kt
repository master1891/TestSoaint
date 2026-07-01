package com.nels.master.testsoaint.data.remote.dto

import com.nels.master.testsoaint.domain.model.Registro

fun RegistroDto.toDomain() = Registro(
    id = id,
    nombre = nombre,
    edad = edad,
    nivelEstudios = nivelEstudios
)

fun Registro.toCrearRequest() = CrearRegistroRequest(
    nombre = nombre,
    edad = edad,
    nivelEstudios = nivelEstudios
)

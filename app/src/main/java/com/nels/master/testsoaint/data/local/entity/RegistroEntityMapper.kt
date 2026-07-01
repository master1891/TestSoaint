package com.nels.master.testsoaint.data.local.entity

import com.nels.master.testsoaint.domain.model.Registro

fun RegistroEntity.toDomain() = Registro(
    id = id,
    nombre = nombre,
    edad = edad,
    nivelEstudios = nivelEstudios,
    fechaCreacion = fechaCreacion,
    sincronizado = sincronizado
)

fun Registro.toEntity() = RegistroEntity(
    id = id,
    nombre = nombre,
    edad = edad,
    nivelEstudios = nivelEstudios,
    fechaCreacion = fechaCreacion,
    sincronizado = sincronizado
)

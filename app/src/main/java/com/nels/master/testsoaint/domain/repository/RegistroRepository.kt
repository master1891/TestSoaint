package com.nels.master.testsoaint.domain.repository

import com.nels.master.testsoaint.domain.model.Registro
import kotlinx.coroutines.flow.Flow

interface RegistroRepository {
    suspend fun crearRegistro(registro: Registro): Result<Registro>
    suspend fun obtenerRegistrosLocales(): Result<List<Registro>>
    fun obtenerRegistrosLocalesFlow(): Flow<List<Registro>>
    suspend fun obtenerRegistrosRemotos(): Result<List<Registro>>
    suspend fun eliminarRegistro(id: Long): Result<Unit>
}

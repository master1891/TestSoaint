package com.nels.master.testsoaint.domain.repository

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import kotlinx.coroutines.flow.Flow

interface RegistroRepository {
    suspend fun crearRegistro(registro: Registro): Resultado<Registro>
    suspend fun obtenerRegistrosLocales(): Resultado<List<Registro>>
    fun obtenerRegistrosLocalesFlow(): Flow<List<Registro>>
    suspend fun obtenerRegistrosRemotos(): Resultado<List<Registro>>
    suspend fun eliminarRegistro(id: Long): Resultado<Unit>
}

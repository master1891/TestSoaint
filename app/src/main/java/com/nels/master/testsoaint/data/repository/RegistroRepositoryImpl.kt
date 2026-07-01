package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.local.dao.RegistroDao
import com.nels.master.testsoaint.data.local.entity.toDomain
import com.nels.master.testsoaint.data.local.entity.toEntity
import com.nels.master.testsoaint.data.remote.api.RegistroApi
import com.nels.master.testsoaint.data.remote.dto.toCrearRequest
import com.nels.master.testsoaint.data.remote.dto.toDomain
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.repository.RegistroRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistroRepositoryImpl @Inject constructor(
    private val registroDao: RegistroDao,
    private val registroApi: RegistroApi
) : RegistroRepository {

    override suspend fun crearRegistro(registro: Registro): Result<Registro> {
        return runCatching {
            val entity = registro.toEntity()
            val id = registroDao.insertar(entity)
            val entityConId = entity.copy(id = id)

            val response = registroApi.crearRegistro(registro.toCrearRequest())
            if (response.isSuccessful) {
                registroDao.actualizarSincronizado(id, true)
                entityConId.copy(sincronizado = true).toDomain()
            } else {
                entityConId.toDomain()
            }
        }
    }

    override suspend fun obtenerRegistrosLocales(): Result<List<Registro>> {
        return runCatching {
            registroDao.obtenerTodos().map { it.toDomain() }
        }
    }

    override suspend fun obtenerRegistrosRemotos(): Result<List<Registro>> {
        return runCatching {
            val response = registroApi.obtenerRegistros()
            if (!response.isSuccessful) {
                throw Exception("Error al obtener registros remotos: ${response.code()}")
            }
            response.body()?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun eliminarRegistro(id: Long): Result<Unit> {
        return runCatching {
            registroDao.eliminarPorId(id)
        }
    }
}

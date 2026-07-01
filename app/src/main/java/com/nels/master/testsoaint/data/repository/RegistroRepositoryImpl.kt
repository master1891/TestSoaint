package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.local.dao.RegistroDao
import com.nels.master.testsoaint.data.local.entity.toDomain
import com.nels.master.testsoaint.data.local.entity.toEntity
import com.nels.master.testsoaint.data.remote.api.RegistroApi
import com.nels.master.testsoaint.data.remote.dto.toCrearRequest
import com.nels.master.testsoaint.data.remote.dto.toDomain
import com.nels.master.testsoaint.domain.exception.AppException
import com.nels.master.testsoaint.domain.exception.NetworkException
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.repository.RegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistroRepositoryImpl @Inject constructor(
    private val registroDao: RegistroDao,
    private val registroApi: RegistroApi
) : RegistroRepository {

    override suspend fun crearRegistro(registro: Registro): Result<Registro> {
        return try {
            val entity = registro.toEntity()
            val id = registroDao.insertar(entity)
            val entityConId = entity.copy(id = id)

            val response = registroApi.crearRegistro(registro.toCrearRequest())
            if (response.isSuccessful) {
                registroDao.actualizarSincronizado(id, true)
                Result.success(entityConId.copy(sincronizado = true).toDomain())
            } else {
                Result.success(entityConId.toDomain())
            }
        } catch (e: AppException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(NetworkException("Error de red", e))
        }
    }

    override suspend fun obtenerRegistrosLocales(): Result<List<Registro>> {
        return runCatching {
            registroDao.obtenerTodos().map { it.toDomain() }
        }
    }

    override fun obtenerRegistrosLocalesFlow(): Flow<List<Registro>> {
        return registroDao.obtenerTodosFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun obtenerRegistrosRemotos(): Result<List<Registro>> {
        return try {
            val response = registroApi.obtenerRegistros()
            if (!response.isSuccessful) {
                Result.failure(NetworkException("Error al obtener registros remotos: ${response.code()}"))
            } else {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            }
        } catch (e: AppException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(NetworkException("Error de red", e))
        }
    }

    override suspend fun eliminarRegistro(id: Long): Result<Unit> {
        return runCatching {
            registroDao.eliminarPorId(id)
        }
    }
}

package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.local.dao.RegistroDao
import com.nels.master.testsoaint.data.local.entity.toDomain
import com.nels.master.testsoaint.data.local.entity.toEntity
import com.nels.master.testsoaint.data.remote.api.RegistroApi
import com.nels.master.testsoaint.data.remote.dto.toCrearRequest
import com.nels.master.testsoaint.data.remote.dto.toDomain
import com.nels.master.testsoaint.domain.exception.NetworkException
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.repository.RegistroRepository
import com.nels.master.testsoaint.domain.resultado.Resultado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistroRepositoryImpl @Inject constructor(
    private val registroDao: RegistroDao,
    private val registroApi: RegistroApi
) : RegistroRepository {

    override suspend fun crearRegistro(registro: Registro): Resultado<Registro> {
        return try {
            val entity = registro.toEntity()
            val id = registroDao.insertar(entity)
            val entityConId = entity.copy(id = id)

            val response = registroApi.crearRegistro(registro.toCrearRequest())
            if (response.isSuccessful) {
                registroDao.actualizarSincronizado(id, true)
                Resultado.Exito(entityConId.copy(sincronizado = true).toDomain())
            } else {
                Resultado.Exito(entityConId.toDomain())
            }
        } catch (e: Exception) {
            Resultado.Error(NetworkException("Error de red", e))
        }
    }

    override suspend fun obtenerRegistrosLocales(): Resultado<List<Registro>> {
        return try {
            Resultado.Exito(registroDao.obtenerTodos().map { it.toDomain() })
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    override fun obtenerRegistrosLocalesFlow(): Flow<List<Registro>> {
        return registroDao.obtenerTodosFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun obtenerRegistrosRemotos(): Resultado<List<Registro>> {
        return try {
            val response = registroApi.obtenerRegistros()
            if (!response.isSuccessful) {
                Resultado.Error(NetworkException("Error al obtener registros remotos: ${response.code()}"))
            } else {
                Resultado.Exito(response.body()?.map { it.toDomain() } ?: emptyList())
            }
        } catch (e: Exception) {
            Resultado.Error(NetworkException("Error de red", e))
        }
    }

    override suspend fun eliminarRegistro(id: Long): Resultado<Unit> {
        return try {
            registroDao.eliminarPorId(id)
            Resultado.Exito(Unit)
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }
}

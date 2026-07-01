package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.remote.api.AuthApi
import com.nels.master.testsoaint.data.remote.dto.LoginRequest
import com.nels.master.testsoaint.domain.exception.AppException
import com.nels.master.testsoaint.domain.exception.AuthenticationException
import com.nels.master.testsoaint.domain.exception.NetworkException
import com.nels.master.testsoaint.domain.exception.TokenException
import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import com.nels.master.testsoaint.utils.JwtUtils
import com.nels.master.testsoaint.utils.PreferencesManager
import com.nels.master.testsoaint.utils.SafeLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Usuario> {
        return try {
            val response = authApi.login(LoginRequest(username, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(AuthenticationException(errorBody))
            } else {
                val token = response.body()?.token
                    ?: return Result.failure(TokenException("Token no recibido"))
                val usuario = JwtUtils.decodificarJwt(token)
                preferencesManager.saveToken(token)
                preferencesManager.saveUser(usuario)
                Result.success(usuario)
            }
        } catch (e: AppException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(NetworkException("Error de conexión", e))
        }
    }

    override fun getSession(): Usuario? {
        val token = preferencesManager.getToken() ?: return null
        return try {
            JwtUtils.decodificarJwt(token)
        } catch (e: Exception) {
            SafeLog.w(TAG, "Error al decodificar sesión: ${e.message}")
            null
        }
    }

    override fun logout() {
        preferencesManager.clear()
    }

    companion object {
        private const val TAG = "AuthRepository"
    }
}

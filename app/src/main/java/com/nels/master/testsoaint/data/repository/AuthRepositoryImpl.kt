package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.remote.api.AuthApi
import com.nels.master.testsoaint.data.remote.dto.LoginRequest
import com.nels.master.testsoaint.domain.exception.AuthenticationException
import com.nels.master.testsoaint.domain.exception.NetworkException
import com.nels.master.testsoaint.domain.exception.TokenException
import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import com.nels.master.testsoaint.domain.repository.JwtDecoder
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.utils.PreferencesManager
import com.nels.master.testsoaint.utils.SafeLog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager,
    private val jwtDecoder: JwtDecoder
) : AuthRepository {

    override suspend fun login(username: String, password: String): Resultado<Usuario> {
        return try {
            val response = authApi.login(LoginRequest(username, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Resultado.Error(AuthenticationException(errorBody))
            } else {
                val token = response.body()?.token
                    ?: return Resultado.Error(TokenException("Token no recibido"))
                val usuario = jwtDecoder.decodificarJwt(token)
                preferencesManager.saveToken(token)
                preferencesManager.saveUser(usuario)
                Resultado.Exito(usuario)
            }
        } catch (e: Exception) {
            Resultado.Error(NetworkException("Error de conexión", e))
        }
    }

    override fun getSession(): Usuario? {
        val token = preferencesManager.getToken() ?: return null
        return try {
            jwtDecoder.decodificarJwt(token)
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

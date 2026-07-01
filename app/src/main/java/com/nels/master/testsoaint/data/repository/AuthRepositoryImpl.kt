package com.nels.master.testsoaint.data.repository

import com.nels.master.testsoaint.data.remote.api.AuthApi
import com.nels.master.testsoaint.data.remote.dto.LoginRequest
import com.nels.master.testsoaint.data.utils.JwtUtils
import com.nels.master.testsoaint.data.utils.PreferencesManager
import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Usuario> {
        return runCatching {
            val response = authApi.login(LoginRequest(username, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                throw Exception("Error de autenticación: $errorBody")
            }
            val token = response.body()?.token ?: throw Exception("Token no recibido")
            val usuario = JwtUtils.decodificarJwt(token)
            preferencesManager.saveToken(token)
            preferencesManager.saveUser(usuario)
            usuario
        }
    }

    override fun getSession(): Usuario? {
        val token = preferencesManager.getToken() ?: return null
        return try {
            JwtUtils.decodificarJwt(token)
        } catch (_: Exception) {
            null
        }
    }

    override fun logout() {
        preferencesManager.clear()
    }
}

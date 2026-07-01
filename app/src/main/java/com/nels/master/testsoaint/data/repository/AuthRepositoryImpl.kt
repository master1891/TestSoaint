package com.nels.master.testsoaint.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.nels.master.testsoaint.data.remote.api.AuthApi
import com.nels.master.testsoaint.data.remote.dto.LoginRequest
import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @param:ApplicationContext private val context: Context
) : AuthRepository {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun login(username: String, password: String): Result<Usuario> {
        return runCatching {
            val response = authApi.login(LoginRequest(username, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                throw Exception("Error de autenticación: $errorBody")
            }
            val token = response.body()?.token ?: throw Exception("Token no recibido")
            val usuario = decodificarJwt(token)
            guardarToken(token)
            guardarUsuario(usuario)
            usuario
        }
    }

    override fun getSession(): Usuario? {
        val token = obtenerToken() ?: return null
        return try {
            decodificarJwt(token)
        } catch (_: Exception) {
            null
        }
    }

    override fun logout() {
        prefs.edit().clear().apply()
    }

    private fun decodificarJwt(token: String): Usuario {
        val parts = token.split(".")
        if (parts.size != 3) throw Exception("JWT inválido")

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val json = Gson().fromJson(payload, Map::class.java)

        val username = json["sub"] as? String ?: throw Exception("JWT sin subject")
        val rol = json["Rol"] as? String ?: throw Exception("JWT sin claim Rol")

        return Usuario(username = username, rol = rol)
    }

    private fun guardarToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    private fun obtenerToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    private fun guardarUsuario(usuario: Usuario) {
        prefs.edit()
            .putString(KEY_USERNAME, usuario.username)
            .putString(KEY_ROL, usuario.rol)
            .apply()
    }

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROL = "rol"
    }
}

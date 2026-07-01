package com.nels.master.testsoaint.data.repository

import android.util.Base64
import com.google.gson.Gson
import com.nels.master.testsoaint.domain.exception.TokenException
import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.JwtDecoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtDecoderImpl @Inject constructor(
    private val gson: Gson
) : JwtDecoder {

    override fun decodificarJwt(token: String): Usuario {
        val parts = token.split(".")
        if (parts.size != 3) throw TokenException("JWT inválido")

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val json = gson.fromJson(payload, Map::class.java)

        val username = json["sub"] as? String ?: throw TokenException("JWT sin subject")
        val rol = json["Rol"] as? String ?: throw TokenException("JWT sin claim Rol")

        return Usuario(username = username, rol = rol)
    }
}

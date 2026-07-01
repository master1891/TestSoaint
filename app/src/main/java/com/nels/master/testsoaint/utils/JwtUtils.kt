package com.nels.master.testsoaint.utils

import android.util.Base64
import com.google.gson.Gson
import com.nels.master.testsoaint.domain.exception.TokenException
import com.nels.master.testsoaint.domain.model.Usuario

object JwtUtils {

    fun decodificarJwt(token: String): Usuario {
        val parts = token.split(".")
        if (parts.size != 3) throw TokenException("JWT inválido")

        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val json = Gson().fromJson(payload, Map::class.java)

        val username = json["sub"] as? String ?: throw TokenException("JWT sin subject")
        val rol = json["Rol"] as? String ?: throw TokenException("JWT sin claim Rol")

        return Usuario(username = username, rol = rol)
    }
}

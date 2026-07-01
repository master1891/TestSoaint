package com.nels.master.testsoaint.domain.repository

import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.resultado.Resultado

interface AuthRepository {
    suspend fun login(username: String, password: String): Resultado<Usuario>
    fun getSession(): Usuario?
    fun logout()
}

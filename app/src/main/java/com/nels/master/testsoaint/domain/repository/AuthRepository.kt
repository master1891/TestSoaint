package com.nels.master.testsoaint.domain.repository

import com.nels.master.testsoaint.domain.model.Usuario

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Usuario>
    fun getSession(): Usuario?
    fun logout()
}

package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Usuario> {
        return authRepository.login(username, password)
    }
}

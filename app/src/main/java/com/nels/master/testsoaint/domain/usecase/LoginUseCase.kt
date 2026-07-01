package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.repository.AuthRepository
import com.nels.master.testsoaint.domain.resultado.Resultado
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Resultado<Usuario> {
        return authRepository.login(username, password)
    }
}

package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.repository.RegistroRepository
import javax.inject.Inject

class EliminarRegistroUseCase @Inject constructor(
    private val registroRepository: RegistroRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return registroRepository.eliminarRegistro(id)
    }
}

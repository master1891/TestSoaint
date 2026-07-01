package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.repository.RegistroRepository
import com.nels.master.testsoaint.domain.resultado.Resultado
import javax.inject.Inject

class EliminarRegistroUseCase @Inject constructor(
    private val registroRepository: RegistroRepository
) {
    suspend operator fun invoke(id: Long): Resultado<Unit> {
        return registroRepository.eliminarRegistro(id)
    }
}

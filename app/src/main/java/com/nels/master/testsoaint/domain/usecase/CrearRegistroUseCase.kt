package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.repository.RegistroRepository
import javax.inject.Inject

class CrearRegistroUseCase @Inject constructor(
    private val registroRepository: RegistroRepository
) {
    suspend operator fun invoke(registro: Registro): Result<Registro> {
        return registroRepository.crearRegistro(registro)
    }
}

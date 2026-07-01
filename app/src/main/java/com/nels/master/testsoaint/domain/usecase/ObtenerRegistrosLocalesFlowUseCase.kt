package com.nels.master.testsoaint.domain.usecase

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.repository.RegistroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerRegistrosLocalesFlowUseCase @Inject constructor(
    private val registroRepository: RegistroRepository
) {
    operator fun invoke(): Flow<List<Registro>> {
        return registroRepository.obtenerRegistrosLocalesFlow()
    }
}

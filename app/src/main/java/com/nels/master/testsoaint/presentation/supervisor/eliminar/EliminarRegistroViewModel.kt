package com.nels.master.testsoaint.presentation.supervisor.eliminar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.usecase.EliminarRegistroUseCase
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EliminarRegistroUiState(
    val registros: List<Registro> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val mensajeExito: String? = null
)

@HiltViewModel
class EliminarRegistroViewModel @Inject constructor(
    private val obtenerRegistrosLocalesFlowUseCase: ObtenerRegistrosLocalesFlowUseCase,
    private val eliminarRegistroUseCase: EliminarRegistroUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EliminarRegistroUiState())
    val uiState: StateFlow<EliminarRegistroUiState> = _uiState.asStateFlow()

    init {
        obtenerRegistrosLocalesFlowUseCase()
            .onEach { registros ->
                _uiState.update { it.copy(registros = registros, isLoading = false) }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Error al cargar registros")
                }
            }
            .launchIn(viewModelScope)
    }

    fun eliminarRegistro(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, mensajeExito = null) }
            val result = eliminarRegistroUseCase(id)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            mensajeExito = "Registro eliminado correctamente"
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al eliminar registro"
                        )
                    }
                }
            )
        }
    }

    fun clearMensaje() {
        _uiState.update { it.copy(mensajeExito = null) }
    }
}

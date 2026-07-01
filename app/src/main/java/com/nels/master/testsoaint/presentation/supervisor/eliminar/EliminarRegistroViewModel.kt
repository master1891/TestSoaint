package com.nels.master.testsoaint.presentation.supervisor.eliminar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.domain.usecase.EliminarRegistroUseCase
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesUseCase
import com.nels.master.testsoaint.utils.SafeLog
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
    private val obtenerRegistrosLocalesFlowUseCase: ObtenerRegistrosLocalesUseCase,
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
                SafeLog.e(TAG, "Error en flow de registros: ${e.message}")
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
            if (result is Resultado.Exito<*>) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensajeExito = "Registro eliminado correctamente"
                    )
                }
            } else if (result is Resultado.Error) {
                SafeLog.e(TAG, "Error al eliminar registro: ${result.exception.message}")
                _uiState.update {
                    it.copy(isLoading = false, error = result.exception.message ?: "Error al eliminar registro")
                }
            }
        }
    }

    companion object {
        private const val TAG = "EliminarRegVM"
    }

    fun clearMensaje() {
        _uiState.update { it.copy(mensajeExito = null) }
    }
}

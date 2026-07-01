package com.nels.master.testsoaint.presentation.operador.remotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosRemotosUseCase
import com.nels.master.testsoaint.utils.SafeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistrosRemotosUiState(
    val registros: List<Registro> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegistrosRemotosViewModel @Inject constructor(
    private val obtenerRegistrosRemotosUseCase: ObtenerRegistrosRemotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrosRemotosUiState())
    val uiState: StateFlow<RegistrosRemotosUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "RegRemotosVM"
    }

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = obtenerRegistrosRemotosUseCase()
            result.fold(
                onSuccess = { registros ->
                    _uiState.update {
                        it.copy(registros = registros, isLoading = false)
                    }
                },
                onFailure = { e ->
                    SafeLog.e(TAG, "Error al cargar registros remotos: ${e.message}")
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Error al cargar registros remotos")
                    }
                }
            )
        }
    }
}

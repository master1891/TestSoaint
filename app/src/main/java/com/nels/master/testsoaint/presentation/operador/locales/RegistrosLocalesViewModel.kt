package com.nels.master.testsoaint.presentation.operador.locales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.model.Registro
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
import javax.inject.Inject

data class RegistrosLocalesUiState(
    val registros: List<Registro> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegistrosLocalesViewModel @Inject constructor(
    private val obtenerRegistrosLocalesUseCase: ObtenerRegistrosLocalesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrosLocalesUiState())
    val uiState: StateFlow<RegistrosLocalesUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "RegLocalesVM"
    }

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        obtenerRegistrosLocalesUseCase()
            .onEach { registros ->
                _uiState.update { it.copy(registros = registros, isLoading = false) }
            }
            .catch { e ->
                SafeLog.e(TAG, "Error al cargar registros locales: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al cargar registros") }
            }
            .launchIn(viewModelScope)
    }
}

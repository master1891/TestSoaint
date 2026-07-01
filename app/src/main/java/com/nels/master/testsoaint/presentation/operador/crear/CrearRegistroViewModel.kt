package com.nels.master.testsoaint.presentation.operador.crear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.domain.usecase.CrearRegistroUseCase
import com.nels.master.testsoaint.utils.SafeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CrearRegistroUiState(
    val nombre: String = "",
    val edad: String = "",
    val nivelEstudios: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val exito: Boolean = false
)

@HiltViewModel
class CrearRegistroViewModel @Inject constructor(
    private val crearRegistroUseCase: CrearRegistroUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearRegistroUiState())
    val uiState: StateFlow<CrearRegistroUiState> = _uiState.asStateFlow()

    fun onNombreChanged(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, error = null) }
    }

    fun onEdadChanged(edad: String) {
        _uiState.update { it.copy(edad = edad, error = null) }
    }

    fun onNivelEstudiosChanged(nivelEstudios: String) {
        _uiState.update { it.copy(nivelEstudios = nivelEstudios, error = null) }
    }

    fun guardar() {
        val state = _uiState.value
        val edad = state.edad.toIntOrNull()

        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(error = "El nombre es requerido") }
            return
        }
        if (edad == null || edad <= 0) {
            _uiState.update { it.copy(error = "Edad inválida") }
            return
        }
        if (state.nivelEstudios.isBlank()) {
            _uiState.update { it.copy(error = "El nivel de estudios es requerido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val registro = Registro(
                nombre = state.nombre,
                edad = edad,
                nivelEstudios = state.nivelEstudios,
                fechaCreacion = System.currentTimeMillis()
            )

            val result = crearRegistroUseCase(registro)
            if (result is Resultado.Exito<*>) {
                _uiState.update { it.copy(isLoading = false, exito = true) }
            } else if (result is Resultado.Error) {
                SafeLog.e(TAG, "Error al guardar registro: ${result.exception.message}")
                _uiState.update {
                    it.copy(isLoading = false, error = result.exception.message ?: "Error al guardar el registro")
                }
            }
        }
    }

    companion object {
        private const val TAG = "CrearRegistroVM"
    }

    fun reiniciar() {
        _uiState.update {
            it.copy(nombre = "", edad = "", nivelEstudios = "", isLoading = false, error = null)
        }
    }

    fun limpiarExito() {
        _uiState.update { it.copy(exito = false) }
    }
}

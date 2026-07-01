package com.nels.master.testsoaint.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nels.master.testsoaint.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val selectedRol: String = "Operador",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val rol: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val usernames = mapOf(
        "Operador" to "operador",
        "Supervisor" to "supervisor"
    )

    fun onRolSelected(rol: String) {
        _uiState.update { it.copy(selectedRol = rol, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login() {
        val state = _uiState.value
        val username = usernames[state.selectedRol] ?: return

        if (state.password.isBlank()) {
            _uiState.update { it.copy(error = "Contraseña requerida") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(username, state.password)
            result.fold(
                onSuccess = { usuario ->
                    _uiState.update { it.copy(isLoading = false, rol = usuario.rol) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error de inicio de sesión"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

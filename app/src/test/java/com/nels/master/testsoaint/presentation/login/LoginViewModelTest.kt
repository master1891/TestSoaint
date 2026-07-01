package com.nels.master.testsoaint.presentation.login

import com.nels.master.testsoaint.domain.model.Usuario
import com.nels.master.testsoaint.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRolSelected updates selected rol`() = runTest {
        viewModel = LoginViewModel(loginUseCase)

        viewModel.onRolSelected("Supervisor")

        assert(viewModel.uiState.value.selectedRol == "Supervisor")
    }

    @Test
    fun `login with blank password shows error`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        viewModel.onRolSelected("Operador")

        viewModel.login()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `login with valid data sets rol on success`() = runTest {
        val usuario = Usuario(username = "operador", rol = "Operador")
        coEvery { loginUseCase.invoke("operador", "123456") } returns Result.success(usuario)

        viewModel = LoginViewModel(loginUseCase)
        viewModel.onPasswordChanged("123456")
        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.rol == "Operador")
    }

    @Test
    fun `login with use case failure sets error`() = runTest {
        coEvery { loginUseCase.invoke(any(), any()) } returns Result.failure(Exception("Error de red"))

        viewModel = LoginViewModel(loginUseCase)
        viewModel.onPasswordChanged("123456")
        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.error != null)
    }
}

package com.nels.master.testsoaint.presentation.operador.remotos

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosRemotosUseCase
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
class RegistrosRemotosViewModelTest {

    private val obtenerRegistrosRemotosUseCase: ObtenerRegistrosRemotosUseCase = mockk()
    private lateinit var viewModel: RegistrosRemotosViewModel
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
    fun `init loads registros from use case`() = runTest {
        val registros = listOf(
            Registro(id = 1, nombre = "Juan", edad = 25, nivelEstudios = "U", fechaCreacion = 1000L)
        )
        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Exito(registros)

        viewModel = RegistrosRemotosViewModel(obtenerRegistrosRemotosUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.size == 1)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `init finishes loading after use case completes`() = runTest {
        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Exito(emptyList())

        viewModel = RegistrosRemotosViewModel(obtenerRegistrosRemotosUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `cargarRegistros on failure sets error`() = runTest {
        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Error(Exception("Network error"))

        viewModel = RegistrosRemotosViewModel(obtenerRegistrosRemotosUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.error != null)
        assert(viewModel.uiState.value.registros.isEmpty())
    }

    @Test
    fun `cargarRegistros with empty list shows empty`() = runTest {
        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Exito(emptyList())

        viewModel = RegistrosRemotosViewModel(obtenerRegistrosRemotosUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.isEmpty())
    }

    @Test
    fun `cargarRegistros retry after error`() = runTest {
        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Error(Exception("First fail"))

        viewModel = RegistrosRemotosViewModel(obtenerRegistrosRemotosUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.error != null)

        coEvery { obtenerRegistrosRemotosUseCase() } returns Resultado.Exito(
            listOf(Registro(id = 1, nombre = "A", edad = 20, nivelEstudios = "U", fechaCreacion = 1000L))
        )

        viewModel.cargarRegistros()
        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.error == null)
        assert(viewModel.uiState.value.registros.size == 1)
    }
}

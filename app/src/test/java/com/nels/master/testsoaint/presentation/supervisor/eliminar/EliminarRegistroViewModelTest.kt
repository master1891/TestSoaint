package com.nels.master.testsoaint.presentation.supervisor.eliminar

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.usecase.EliminarRegistroUseCase
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesUseCase
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
class EliminarRegistroViewModelTest {

    private val obtenerRegistrosLocalesUseCase: ObtenerRegistrosLocalesUseCase = mockk()
    private val eliminarRegistroUseCase: EliminarRegistroUseCase = mockk()
    private lateinit var viewModel: EliminarRegistroViewModel
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
    fun `init loads registros`() = runTest {
        val registros = listOf(
            Registro(id = 1, nombre = "Juan", edad = 25, nivelEstudios = "U")
        )
        coEvery { obtenerRegistrosLocalesUseCase() } returns Result.success(registros)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.size == 1)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `eliminarRegistro on success shows mensaje and reloads`() = runTest {
        coEvery { obtenerRegistrosLocalesUseCase() } returns Result.success(emptyList())
        coEvery { eliminarRegistroUseCase(1) } returns Result.success(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.mensajeExito != null)
    }

    @Test
    fun `eliminarRegistro on failure sets error`() = runTest {
        coEvery { obtenerRegistrosLocalesUseCase() } returns Result.success(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Result.failure(Exception("Error"))

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `clearMensaje clears success message`() = runTest {
        coEvery { obtenerRegistrosLocalesUseCase() } returns Result.success(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Result.success(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.clearMensaje()

        assert(viewModel.uiState.value.mensajeExito == null)
    }
}

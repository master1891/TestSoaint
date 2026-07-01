package com.nels.master.testsoaint.presentation.supervisor.eliminar

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.usecase.EliminarRegistroUseCase
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesFlowUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EliminarRegistroViewModelTest {

    private val obtenerRegistrosLocalesFlowUseCase: ObtenerRegistrosLocalesFlowUseCase = mockk()
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
    fun `init loads registros from flow`() = runTest {
        val registros = listOf(
            Registro(id = 1, nombre = "Juan", edad = 25, nivelEstudios = "U")
        )
        every { obtenerRegistrosLocalesFlowUseCase() } returns flowOf(registros)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesFlowUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.size == 1)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `eliminarRegistro on success shows mensaje`() = runTest {
        every { obtenerRegistrosLocalesFlowUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(1) } returns Result.success(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesFlowUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.mensajeExito != null)
    }

    @Test
    fun `eliminarRegistro on failure sets error`() = runTest {
        every { obtenerRegistrosLocalesFlowUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Result.failure(Exception("Error"))

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesFlowUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `clearMensaje clears success message`() = runTest {
        every { obtenerRegistrosLocalesFlowUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Result.success(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesFlowUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.clearMensaje()

        assert(viewModel.uiState.value.mensajeExito == null)
    }
}

package com.nels.master.testsoaint.presentation.supervisor.eliminar

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.domain.usecase.EliminarRegistroUseCase
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesUseCase
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
    fun `init loads registros from flow`() = runTest {
        val registros = listOf(
            Registro(id = 1, nombre = "Juan", edad = 25, nivelEstudios = "Superior", fechaCreacion = 1000L)
        )
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(registros)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.size == 1)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `eliminarRegistro on success shows mensaje`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(1) } returns Resultado.Exito(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.mensajeExito != null)
    }

    @Test
    fun `eliminarRegistro on failure sets error`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Resultado.Error(Exception("Error"))

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `clearMensaje clears success message`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())
        coEvery { eliminarRegistroUseCase(any()) } returns Resultado.Exito(Unit)

        viewModel = EliminarRegistroViewModel(obtenerRegistrosLocalesUseCase, eliminarRegistroUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eliminarRegistro(1)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.clearMensaje()

        assert(viewModel.uiState.value.mensajeExito == null)
    }
}

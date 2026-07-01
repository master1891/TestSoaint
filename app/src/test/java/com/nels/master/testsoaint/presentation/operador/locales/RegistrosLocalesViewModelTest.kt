package com.nels.master.testsoaint.presentation.operador.locales

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.usecase.ObtenerRegistrosLocalesUseCase
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
class RegistrosLocalesViewModelTest {

    private val obtenerRegistrosLocalesUseCase: ObtenerRegistrosLocalesUseCase = mockk()
    private lateinit var viewModel: RegistrosLocalesViewModel
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
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(registros)

        viewModel = RegistrosLocalesViewModel(obtenerRegistrosLocalesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.size == 1)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `init finishes loading after use case completes`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())

        viewModel = RegistrosLocalesViewModel(obtenerRegistrosLocalesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun `cargarRegistros on failure sets error`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())

        viewModel = RegistrosLocalesViewModel(obtenerRegistrosLocalesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.error == null)
    }

    @Test
    fun `cargarRegistros with empty list shows empty`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(emptyList())

        viewModel = RegistrosLocalesViewModel(obtenerRegistrosLocalesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value.registros.isEmpty())
    }

    @Test
    fun `cargarRegistros can be called multiple times`() = runTest {
        every { obtenerRegistrosLocalesUseCase() } returns flowOf(
            listOf(Registro(id = 1, nombre = "A", edad = 20, nivelEstudios = "U", fechaCreacion = 1000L))
        )

        viewModel = RegistrosLocalesViewModel(obtenerRegistrosLocalesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.registros.size == 1)

        every { obtenerRegistrosLocalesUseCase() } returns flowOf(
            listOf(
                Registro(id = 1, nombre = "A", edad = 20, nivelEstudios = "U", fechaCreacion = 1000L),
                Registro(id = 2, nombre = "B", edad = 30, nivelEstudios = "S", fechaCreacion = 2000L)
            )
        )

        viewModel.cargarRegistros()
        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.registros.size == 2)
    }
}

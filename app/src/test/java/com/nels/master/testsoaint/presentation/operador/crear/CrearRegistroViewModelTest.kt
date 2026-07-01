package com.nels.master.testsoaint.presentation.operador.crear

import com.nels.master.testsoaint.domain.model.Registro
import com.nels.master.testsoaint.domain.resultado.Resultado
import com.nels.master.testsoaint.domain.usecase.CrearRegistroUseCase
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
class CrearRegistroViewModelTest {

    private val crearRegistroUseCase: CrearRegistroUseCase = mockk()
    private lateinit var viewModel: CrearRegistroViewModel
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
    fun `guardar with blank nombre shows error`() = runTest {
        viewModel = CrearRegistroViewModel(crearRegistroUseCase)

        viewModel.guardar()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `guardar with invalid edad shows error`() = runTest {
        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")
        viewModel.onEdadChanged("abc")

        viewModel.guardar()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `guardar with negative edad shows error`() = runTest {
        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")
        viewModel.onEdadChanged("-5")

        viewModel.guardar()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `guardar with blank nivelEstudios shows error`() = runTest {
        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")
        viewModel.onEdadChanged("25")

        viewModel.guardar()

        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `guardar with valid data sets exito on success`() = runTest {
        coEvery { crearRegistroUseCase.invoke(any<Registro>()) } returns Resultado.Exito(
            Registro(nombre = "Juan", edad = 25, nivelEstudios = "Universitario", fechaCreacion = 1000L)
        )

        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")
        viewModel.onEdadChanged("25")
        viewModel.onNivelEstudiosChanged("Universitario")
        viewModel.guardar()

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.exito)
    }

    @Test
    fun `guardar with use case failure sets error`() = runTest {
        coEvery { crearRegistroUseCase.invoke(any<Registro>()) } returns Resultado.Error(Exception("Error"))

        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")
        viewModel.onEdadChanged("25")
        viewModel.onNivelEstudiosChanged("Universitario")
        viewModel.guardar()

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value.error != null)
    }

    @Test
    fun `reiniciar resets state`() = runTest {
        viewModel = CrearRegistroViewModel(crearRegistroUseCase)
        viewModel.onNombreChanged("Juan")

        viewModel.reiniciar()

        assert(viewModel.uiState.value.nombre == "")
        assert(!viewModel.uiState.value.exito)
    }
}

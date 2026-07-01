package com.nels.master.testsoaint.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.nels.master.testsoaint.presentation.login.LoginScreen
import com.nels.master.testsoaint.presentation.login.LoginViewModel
import com.nels.master.testsoaint.presentation.operador.crear.CrearRegistroScreen
import com.nels.master.testsoaint.presentation.operador.crear.CrearRegistroViewModel
import com.nels.master.testsoaint.presentation.operador.locales.RegistrosLocalesScreen
import com.nels.master.testsoaint.presentation.operador.locales.RegistrosLocalesViewModel
import com.nels.master.testsoaint.presentation.operador.menu.OperadorMenuScreen
import com.nels.master.testsoaint.presentation.operador.remotos.RegistrosRemotosScreen
import com.nels.master.testsoaint.presentation.operador.remotos.RegistrosRemotosViewModel
import com.nels.master.testsoaint.presentation.supervisor.eliminar.EliminarRegistroScreen
import com.nels.master.testsoaint.presentation.supervisor.eliminar.EliminarRegistroViewModel
import com.nels.master.testsoaint.presentation.supervisor.menu.SupervisorMenuScreen
import com.nels.master.testsoaint.ui.theme.ToolbarOperador
import com.nels.master.testsoaint.ui.theme.ToolbarSupervisor

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    initialRol: String? = null,
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentRol by remember { mutableStateOf(initialRol) }
    val toolbarColor = if (currentRol == "Operador") ToolbarOperador else ToolbarSupervisor

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { rol ->
                    currentRol = rol
                    navController.navigate(
                        if (rol == "Supervisor") SupervisorMenu else OperadorMenu,
                        navOptions {
                            popUpTo(Login::class.qualifiedName!!) { inclusive = true }
                        }
                    )
                }
            )
        }

        composable<OperadorMenu> {
            OperadorMenuScreen(
                toolbarContainerColor = toolbarColor,
                onCrearRegistro = { navController.navigate(CrearRegistro) },
                onVerLocales = { navController.navigate(RegistrosLocales) },
                onVerRemotos = { navController.navigate(RegistrosRemotos) },
                onLogout = onLogout
            )
        }

        composable<CrearRegistro> {
            val viewModel: CrearRegistroViewModel = hiltViewModel()
            CrearRegistroScreen(
                toolbarContainerColor = toolbarColor,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosLocales> {
            val viewModel: RegistrosLocalesViewModel = hiltViewModel()
            RegistrosLocalesScreen(
                toolbarContainerColor = toolbarColor,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosRemotos> {
            val viewModel: RegistrosRemotosViewModel = hiltViewModel()
            RegistrosRemotosScreen(
                toolbarContainerColor = toolbarColor,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<SupervisorMenu> {
            SupervisorMenuScreen(
                toolbarContainerColor = toolbarColor,
                onEliminarRegistro = { navController.navigate(EliminarRegistro) },
                onLogout = onLogout
            )
        }

        composable<EliminarRegistro> {
            val viewModel: EliminarRegistroViewModel = hiltViewModel()
            EliminarRegistroScreen(
                toolbarContainerColor = toolbarColor,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

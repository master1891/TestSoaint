package com.nels.master.testsoaint.presentation.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                onCrearRegistro = { navController.navigate(CrearRegistro) },
                onVerLocales = { navController.navigate(RegistrosLocales) },
                onVerRemotos = { navController.navigate(RegistrosRemotos) },
                onLogout = onLogout
            )
        }

        composable<CrearRegistro> {
            val viewModel: CrearRegistroViewModel = hiltViewModel()
            CrearRegistroScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosLocales> {
            val viewModel: RegistrosLocalesViewModel = hiltViewModel()
            RegistrosLocalesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosRemotos> {
            val viewModel: RegistrosRemotosViewModel = hiltViewModel()
            RegistrosRemotosScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<SupervisorMenu> {
            SupervisorMenuScreen(
                onEliminarRegistro = { navController.navigate(EliminarRegistro) },
                onLogout = onLogout
            )
        }

        composable<EliminarRegistro> {
            val viewModel: EliminarRegistroViewModel = hiltViewModel()
            EliminarRegistroScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

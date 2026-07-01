package com.nels.master.testsoaint.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nels.master.testsoaint.presentation.login.LoginScreen
import com.nels.master.testsoaint.presentation.operador.crear.CrearRegistroScreen
import com.nels.master.testsoaint.presentation.operador.locales.RegistrosLocalesScreen
import com.nels.master.testsoaint.presentation.operador.menu.OperadorMenuScreen
import com.nels.master.testsoaint.presentation.operador.remotos.RegistrosRemotosScreen
import com.nels.master.testsoaint.presentation.supervisor.eliminar.EliminarRegistroScreen
import com.nels.master.testsoaint.presentation.supervisor.menu.SupervisorMenuScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { rol ->
                    val destino = if (rol == "Supervisor") {
                        Screen.SupervisorMenu.route
                    } else {
                        Screen.OperadorMenu.route
                    }
                    navController.navigate(destino) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OperadorMenu.route) {
            OperadorMenuScreen(
                onCrearRegistro = { navController.navigate(Screen.CrearRegistro.route) },
                onVerLocales = { navController.navigate(Screen.RegistrosLocales.route) },
                onVerRemotos = { navController.navigate(Screen.RegistrosRemotos.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CrearRegistro.route) {
            CrearRegistroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RegistrosLocales.route) {
            RegistrosLocalesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RegistrosRemotos.route) {
            RegistrosRemotosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SupervisorMenu.route) {
            SupervisorMenuScreen(
                onEliminarRegistro = { navController.navigate(Screen.EliminarRegistro.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EliminarRegistro.route) {
            EliminarRegistroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

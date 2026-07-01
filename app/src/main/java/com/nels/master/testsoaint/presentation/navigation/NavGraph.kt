package com.nels.master.testsoaint.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
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
        composable<Login> {
            LoginScreen(
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
                onLogout = {
                    navController.navigate(Login, navOptions {
                        popUpTo(0) { inclusive = true }
                    })
                }
            )
        }

        composable<CrearRegistro> {
            CrearRegistroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosLocales> {
            RegistrosLocalesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<RegistrosRemotos> {
            RegistrosRemotosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<SupervisorMenu> {
            SupervisorMenuScreen(
                onEliminarRegistro = { navController.navigate(EliminarRegistro) },
                onLogout = {
                    navController.navigate(Login, navOptions {
                        popUpTo(0) { inclusive = true }
                    })
                }
            )
        }

        composable<EliminarRegistro> {
            EliminarRegistroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

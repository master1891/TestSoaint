package com.nels.master.testsoaint.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object OperadorMenu : Screen("operador/menu")
    data object CrearRegistro : Screen("operador/crear")
    data object RegistrosLocales : Screen("operador/locales")
    data object RegistrosRemotos : Screen("operador/remotos")
    data object SupervisorMenu : Screen("supervisor/menu")
    data object EliminarRegistro : Screen("supervisor/eliminar")
}

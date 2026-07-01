package com.nels.master.testsoaint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nels.master.testsoaint.domain.repository.AuthRepository
import com.nels.master.testsoaint.presentation.navigation.Login
import com.nels.master.testsoaint.presentation.navigation.NavGraph
import com.nels.master.testsoaint.presentation.navigation.OperadorMenu
import com.nels.master.testsoaint.presentation.navigation.SupervisorMenu
import com.nels.master.testsoaint.ui.theme.TestSoaintTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestSoaintTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val sesion = authRepository.getSession()
                    val startDestination = if (sesion != null) {
                        if (sesion.rol == "Supervisor") SupervisorMenu::class.qualifiedName!!
                        else OperadorMenu::class.qualifiedName!!
                    } else {
                        Login::class.qualifiedName!!
                    }

                    val onLogout = {
                        authRepository.logout()
                        navController.navigate(Login::class.qualifiedName!!) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        initialRol = sesion?.rol,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

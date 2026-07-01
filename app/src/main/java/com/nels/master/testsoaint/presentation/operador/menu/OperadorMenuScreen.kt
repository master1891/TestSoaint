package com.nels.master.testsoaint.presentation.operador.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OperadorMenuScreen(
    onCrearRegistro: () -> Unit,
    onVerLocales: () -> Unit,
    onVerRemotos: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Menú Operador",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCrearRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Opción 1: Dar de alta un registro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerLocales,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Opción 2: Consultar transacciones locales")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerRemotos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Opción 3: Consultar registros remotos")
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}

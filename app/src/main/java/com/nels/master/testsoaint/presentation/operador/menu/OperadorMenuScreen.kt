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
import androidx.compose.ui.res.stringResource
import com.nels.master.testsoaint.R
import com.nels.master.testsoaint.ui.theme.Dimens

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
            .padding(Dimens.paddingScreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.menu_operador_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(Dimens.spacingXxl))

        Button(
            onClick = onCrearRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.menu_operador_opcion1))
        }

        Spacer(modifier = Modifier.height(Dimens.spacingLg))

        Button(
            onClick = onVerLocales,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.menu_operador_opcion2))
        }

        Spacer(modifier = Modifier.height(Dimens.spacingLg))

        Button(
            onClick = onVerRemotos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.menu_operador_opcion3))
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.cerrar_sesion))
        }
    }
}

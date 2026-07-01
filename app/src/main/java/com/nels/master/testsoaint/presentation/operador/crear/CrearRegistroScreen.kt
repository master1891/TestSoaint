package com.nels.master.testsoaint.presentation.operador.crear

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nels.master.testsoaint.ui.theme.Dimens
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nels.master.testsoaint.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRegistroScreen(
    toolbarContainerColor: Color,
    viewModel: CrearRegistroViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var nivelEstudios by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    val exitoMsg = stringResource(R.string.crear_exito)

    LaunchedEffect(state.exito) {
        if (state.exito) {
            viewModel.reiniciar()
            nombre = ""
            edad = ""
            nivelEstudios = ""
            snackbarHostState.showSnackbar(exitoMsg)
            viewModel.limpiarExito()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.crear_titulo)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = toolbarContainerColor
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.regresar)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.paddingScreen)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    viewModel.onNombreChanged(it)
                },
                label = { Text(stringResource(R.string.crear_nombre)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLg))

            OutlinedTextField(
                value = edad,
                onValueChange = {
                    edad = it
                    viewModel.onEdadChanged(it)
                },
                label = { Text(stringResource(R.string.crear_edad)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(Dimens.spacingLg))

            OutlinedTextField(
                value = nivelEstudios,
                onValueChange = {
                    nivelEstudios = it
                    viewModel.onNivelEstudiosChanged(it)
                },
                label = { Text(stringResource(R.string.crear_estudios)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(Dimens.paddingScreen))

            Button(
                onClick = { viewModel.guardar() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(Dimens.spacingXl),
                        strokeWidth = Dimens.strokeButton,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.crear_guardar))
                }
            }


        }
    }
}

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRegistroScreen(
    onNavigateBack: () -> Unit,
    viewModel: CrearRegistroViewModel = hiltViewModel()
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

    LaunchedEffect(state.exito) {
        if (state.exito) {
            viewModel.reiniciar()
            nombre = ""
            edad = ""
            nivelEstudios = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dar de alta un registro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    viewModel.onNombreChanged(it)
                },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = edad,
                onValueChange = {
                    edad = it
                    viewModel.onEdadChanged(it)
                },
                label = { Text("Edad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nivelEstudios,
                onValueChange = {
                    nivelEstudios = it
                    viewModel.onNivelEstudiosChanged(it)
                },
                label = { Text("Nivel de estudios") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.guardar() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar")
                }
            }

            if (state.exito) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Registro guardado exitosamente",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

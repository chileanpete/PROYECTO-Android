package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalAddPlatoScreen(
    navController: NavController,
    viewModel: LocalPlatoVeiwModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.platoUiState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar Categoría") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = uiState.platoData.nombre,
                onValueChange = { viewModel.updateUiState(uiState.platoData.copy(nombre = it)) },
                label = { Text("Nombre") },
                isError = uiState.platoData.nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.platoData.nombre.isBlank()) {
                Text(
                    text = "El nombre es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.platoData.descripcion,
                onValueChange = { viewModel.updateUiState(uiState.platoData.copy(descripcion = it)) },
                label = { Text("Descripción") },
                isError = uiState.platoData.descripcion.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            if (uiState.platoData.descripcion.isBlank()) {
                Text(
                    text = "La descripción es obligatoria",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (uiState.isEntryValid) {
                        viewModel.savePlato()
                        scope.launch {
                            snackbarHostState.showSnackbar("Categoría guardada")
                        }
                        navController.popBackStack()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa todos los campos correctamente")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isEntryValid
            ) {
                Text("Guardar")
            }
        }
    }
}
package com.example.proyecto_droid.ui.screens.lugares

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalAddLugarScreen(
    navController: NavController,
    viewModel: LocalLugarViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState: LugarUiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo Lugar") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nombre (requerido)
            OutlinedTextField(
                value = uiState.lugarData.nombre,
                onValueChange = { viewModel.updateUiState(uiState.lugarData.copy(nombre = it)) },
                label = { Text("Nombre*") },
                isError = uiState.lugarData.nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Tipo (ej: Restaurante, Café, etc.)
            OutlinedTextField(
                value = uiState.lugarData.tipo,
                onValueChange = { viewModel.updateUiState(uiState.lugarData.copy(tipo = it)) },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Ubicación
            OutlinedTextField(
                value = uiState.lugarData.ubicacion ?: "",
                onValueChange = { viewModel.updateUiState(uiState.lugarData.copy(ubicacion = it.ifBlank { null })) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Teléfono
            OutlinedTextField(
                value = uiState.lugarData.telefono ?: "",
                onValueChange = { viewModel.updateUiState(uiState.lugarData.copy(telefono = it.ifBlank { null })) },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // Botón Guardar
            Button(
                onClick = {
                    if (uiState.isEntryValid) {
                        scope.launch {
                            viewModel.saveLugar()
                            snackbarHostState.showSnackbar("Lugar guardado")
                            navController.popBackStack()
                        }
                    }
                },
                enabled = uiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Lugar")
            }
        }
    }
}
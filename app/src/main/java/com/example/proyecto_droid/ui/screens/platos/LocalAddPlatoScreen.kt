package com.example.proyecto_droid.ui.screens.platos

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
fun LocalAddPlatoScreen(
    navController: NavController,
    viewModel: LocalPlatoVeiwModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.platoUiState
    val lugares by viewModel.lugares.collectAsState(initial = emptyList())
    val categorias by viewModel.categorias.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo Plato") }) },
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Campo Nombre
            OutlinedTextField(
                value = uiState.platoData.nombre,
                onValueChange = { viewModel.updateUiState(uiState.platoData.copy(nombre = it)) },
                label = { Text("Nombre del plato") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Categoría (requerido)
            var expandedCategoria by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }
            ) {
                OutlinedTextField(
                    value = categorias.find { it.id_categoria == uiState.platoData.idCategoria }?.nombre ?: "Seleccione categoría",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                viewModel.updateUiState(uiState.platoData.copy(idCategoria = categoria.id_categoria))
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            // Precio
            OutlinedTextField(
                value = uiState.platoData.precio.toString(),
                onValueChange = { viewModel.updateUiState(uiState.platoData.copy(precio = it.toDoubleOrNull() ?: 0.0)) },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Botón Guardar
            Button(
                onClick = {
                    if (uiState.isEntryValid) {
                        viewModel.savePlato()
                        navController.popBackStack()
                    }
                },
                enabled = uiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
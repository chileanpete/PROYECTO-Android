package com.example.proyecto_droid.ui.screens.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.AppViewModelProvider
import com.example.proyecto_droid.ui.screens.lugares.LugarUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoriaScreen(
    navController: NavController,
    viewModel: CategoriaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState: CategoriaUiState by viewModel.formState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Categoría") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = uiState.categoriaData.nombre,
                onValueChange = { newName ->
                    viewModel.updateUiState(uiState.categoriaData.copy(nombre = newName))
                },
                label = { Text("Nombre*") },
                isError = uiState.categoriaData.nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.categoriaData.nombre.isBlank()) {
                Text(
                    text = "El nombre es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Descripción
            OutlinedTextField(
                value = uiState.categoriaData.descripcion ?: "",  // Provee un valor por defecto
                onValueChange = { newDesc ->
                    viewModel.updateUiState(uiState.categoriaData.copy(
                        descripcion = newDesc.ifBlank { null }  // Convierte a null si está vacío
                    ))
                },
                label = { Text("Descripción*") },
                isError = uiState.categoriaData.descripcion?.isBlank() ?: true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            if (uiState.categoriaData.descripcion?.isBlank() == true) {
                Text(
                    text = "La descripción es obligatoria",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón para subir imagen (opcional)
            /*
            Button(
                onClick = { /* Implementar selección de imagen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Image, contentDescription = "Imagen")
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar imagen")
            }
            */

            // Botón Guardar
            Button(
                onClick = {
                    if (uiState.isEntryValid) {
                        scope.launch {
                            viewModel.saveCategoria()
                            snackbarHostState.showSnackbar("Categoría guardada")
                            navController.popBackStack()
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Complete los campos requeridos")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isEntryValid
            ) {
                Text("Guardar Categoría")
            }
        }
    }
}
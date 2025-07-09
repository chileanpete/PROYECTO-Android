package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.screens.platos.LocalPlatoVeiwModel
import com.example.proyecto_droid.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalPlatoScreen(
    navController: NavController,
    viewModel: LocalPlatoVeiwModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observa la lista de categorías del StateFlow
    val platos by viewModel.platos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Categorías") })
        },

        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (platos.isEmpty()) {
                Text("No hay categorías", style = MaterialTheme.typography.bodyLarge)
            } else {
                platos.forEach { plato ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                // Mostrar contenidos de esa categoría
                                navController.navigate("contenido/${categoria.id}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(categoria.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(categoria.descripcion, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("agregar_contenido/${categoria.id}")
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Contenido")
                            }

                            FloatingActionButton(
                                onClick = {
                                    viewModel.deletePlato(plato)
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Eliminar Categoría")
                            }
                        }
                    }
                }

            }
        }
    }
}
package com.example.proyecto_droid.ui.screens.lugares

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.screens.lugares.LocalLugarViewModel
import com.example.proyecto_droid.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalLugarScreen(
    navController: NavController,
    viewModel: LocalLugarViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observa la lista de categorías del StateFlow
    val lugares by viewModel.lugares.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Lugares") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_lugar") }
            ) {
                Icon(Icons.Default.Add, "Agregar Lugar")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (lugares.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay lugares registrados")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(lugares) { lugar ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { /* Editar plato */ }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(lugar.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Tipo: $${lugar.tipo}")
                            Text("Calificacion Promedio: ${lugar.calificacion_promedio}")  // Mejorar con nombre de categoría
                            Row {
                                IconButton(onClick = { viewModel.deleteLugar(lugar) }) {
                                    Icon(Icons.Default.Delete, "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.example.proyecto_droid.ui.screens.platos

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
        topBar = { TopAppBar(title = { Text("Platos") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_plato") }
            ) {
                Icon(Icons.Default.Add, "Agregar Plato")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (platos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay platos registrados")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(platos) { plato ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { /* Editar plato */ }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(plato.nombrePlato, style = MaterialTheme.typography.titleMedium)
                            Text("Precio: $${plato.precioPlato}")
                            Text("Categoría: ${plato.idCategoria}")  // Mejorar con nombre de categoría
                            Row {
                                IconButton(onClick = { viewModel.deletePlato(plato) }) {
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
package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.ui.AddPlato

@Composable
fun PlatosScreen(
    viewModel: PlatosViewModel = viewModel(
        factory = com.example.proyecto_droid.ui.AppViewModelProvider.Factory
    ),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.refreshPlatos()
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate(AddPlato)}) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
            }
        }
    ) {
            innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val state = viewModel.platosUiState) {
                is PlatosUiState.Loading -> {
                    Text(text = "Cargando patos...")
                }

                is PlatosUiState.Error -> {
                    Text(text = "Error: ${state.message}")
                }

                is PlatosUiState.Success -> {
                    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                        items(state.platos.size) { index ->
                            PlatoCard(plato = state.platos[index])
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun PlatoCard(
    plato: Plato,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(text = "ID: ${plato.id}")
            Text(text = "Nombre: ${plato.nombrePlato}")
            Text(text = "Descripción: ${plato.descripcionPlato ?: "No hay descripción"}")
        }
    }
}
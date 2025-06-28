package com.example.proyecto_droid.ui.screens.lugares

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
import com.example.proyecto_droid.data.model.Lugar
import com.example.proyecto_droid.ui.AddLugar
import com.example.proyecto_droid.ui.screens.lugares.LugaresUiState
import com.example.proyecto_droid.ui.screens.lugares.LugaresViewModel

@Composable
fun LugaresScreen(
    viewModel: LugaresViewModel = viewModel(
        factory = com.example.proyecto_droid.ui.AppViewModelProvider.Factory
    ),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.refreshLugares()
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate(AddLugar)}) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
            }
        }
    ) {
            innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val state = viewModel.lugaresUiState) {
                is LugaresUiState.Loading -> {
                    Text(text = "Cargando patos...")
                }

                is LugaresUiState.Error -> {
                    Text(text = "Error: ${state.message}")
                }

                is LugaresUiState.Success -> {
                    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                        items(state.lugares.size) { index ->
                            LugarCard(lugar = state.lugares[index])
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun LugarCard(
    lugar: Lugar,
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
            Text(text = "ID: ${lugar.id}")
            Text(text = "Nombre: ${lugar.nombre}")
            Text(text = "Tipo: ${lugar.tipo ?: "No hay descripción"}")
        }
    }
}
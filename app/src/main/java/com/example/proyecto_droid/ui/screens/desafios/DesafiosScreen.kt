package com.example.proyecto_droid.ui.screens.desafios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto_droid.model.Desafio
import com.example.proyecto_droid.model.TipoDesafio
import com.example.proyecto_droid.ui.screens.desafios.components.DesafioCard
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.viewmodel.DesafiosViewModel

@Composable
fun DesafiosScreenTab(viewModel: DesafiosViewModel = viewModel()) {
    val mostrarDialogo = remember { mutableStateOf(true) }
    val seleccionados = remember { mutableStateListOf<String>() }

    val desafios by viewModel.desafios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.error.collectAsState()

    val objetivosDisponibles = listOf(
        "Mejorar salud cardiovascular",
        "Perder peso",
        "Aumentar fuerza muscular",
        "Mejorar flexibilidad",
        "Reducir estrés"
    )

    LaunchedEffect(mostrarDialogo.value) {
        if (!mostrarDialogo.value) {
            viewModel.cargarDesafios()
        }
    }

    val desafiosFiltrados = desafios.filter { desafio ->
        desafio.objetivosRelacionados.any { it in seleccionados }
    }

    val desafiosDiarios = desafiosFiltrados.filter { it.tipo == TipoDesafio.DIARIO }
    val desafiosSemanales = desafiosFiltrados.filter { it.tipo == TipoDesafio.SEMANAL }

    if (mostrarDialogo.value) {
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 6.dp,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿Cuál es tu objetivo?",
                        style = MaterialTheme.typography.titleLarge,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona uno o más para sugerirte desafíos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    objetivosDisponibles.forEach { objetivo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (objetivo in seleccionados) {
                                        seleccionados.remove(objetivo)
                                    } else {
                                        seleccionados.add(objetivo)
                                    }
                                }
                        ) {
                            Checkbox(
                                checked = objetivo in seleccionados,
                                onCheckedChange = {
                                    if (it) seleccionados.add(objetivo)
                                    else seleccionados.remove(objetivo)
                                }
                            )
                            Text(objetivo)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { if (seleccionados.isNotEmpty()) mostrarDialogo.value = false },
                    enabled = seleccionados.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    if (!mostrarDialogo.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Desafíos sugeridos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMsg != null) {
                Text(
                    text = errorMsg ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    // Mostrar encabezado y lista de desafíos diarios
                    if (desafiosDiarios.isNotEmpty()) {
                        item {
                            Text(
                                text = "Desafíos Diarios",
                                style = MaterialTheme.typography.titleLarge.copy(color = GreenPrimary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(desafiosDiarios) { desafio ->
                            DesafioCard(desafio)
                        }
                    }

                    // Mostrar encabezado y lista de desafíos semanales
                    if (desafiosSemanales.isNotEmpty()) {
                        item {
                            Text(
                                text = "Desafíos Semanales",
                                style = MaterialTheme.typography.titleLarge.copy(color = GreenPrimary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(desafiosSemanales) { desafio ->
                            DesafioCard(desafio)
                        }
                    }

                    if (desafiosDiarios.isEmpty() && desafiosSemanales.isEmpty()) {
                        item {
                            Text(
                                text = "No hay desafíos para los objetivos seleccionados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DesafiosScreen() {
    DesafiosScreenTab()
}

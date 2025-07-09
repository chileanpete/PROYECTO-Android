package com.example.proyecto_droid.ui.screens.desafios

import android.app.Application
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.data.local.DesafioPreferences
import com.example.proyecto_droid.data.model.TipoDesafio
import com.example.proyecto_droid.ui.screens.desafios.components.DesafioCard
import com.example.proyecto_droid.ui.viewmodel.DesafiosViewModel
import kotlinx.coroutines.launch

@Composable
fun DesafiosScreenTab(
    viewModel: DesafiosViewModel = viewModel(),
    idUsuario: Int = 1 // TODO: Reemplaza esto por el id_usuario real del usuario logueado
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarDesafios()
    }

    // Objetivos disponibles
    val objetivosDisponibles = listOf(
        "Mejorar salud cardiovascular",
        "Perder peso",
        "Aumentar fuerza muscular",
        "Mejorar flexibilidad",
        "Reducir estrés",
        "Consistencia"
    )

    // Estado de objetivos semanales y diarios
    var objetivosSemana by remember { mutableStateOf<List<String>?>(null) }
    var fechaSemana by remember { mutableStateOf<String?>(null) }
    var objetivosDia by remember { mutableStateOf<List<String>?>(null) }
    var fechaDia by remember { mutableStateOf<String?>(null) }

    // Estado para mostrar los diálogos
    var mostrarDialogoSemana by remember { mutableStateOf(false) }
    var mostrarDialogoDia by remember { mutableStateOf(false) }
    val semanaActual = DesafioPreferences.getCurrentWeekStart()
    val diaActual = DesafioPreferences.getCurrentDay()

    // Cargar objetivos guardados
    LaunchedEffect(idUsuario) {
        DesafioPreferences.getWeeklyObjectives(context, idUsuario).collect { (objs, fecha) ->
            objetivosSemana = objs
            fechaSemana = fecha
            if (fecha != semanaActual) mostrarDialogoSemana = true
        }
    }
    LaunchedEffect(idUsuario) {
        DesafioPreferences.getDailyObjectives(context, idUsuario).collect { (objs, fecha) ->
            objetivosDia = objs
            fechaDia = fecha
            if (fecha != diaActual) mostrarDialogoDia = true
        }
    }

    // Guardar objetivos semanales
    fun guardarObjetivosSemana(seleccionados: List<String>) {
        scope.launch {
            DesafioPreferences.saveWeeklyObjectives(context, idUsuario, seleccionados, semanaActual)
            objetivosSemana = seleccionados
            fechaSemana = semanaActual
            mostrarDialogoSemana = false
        }
    }
    // Guardar objetivos diarios
    fun guardarObjetivosDia(seleccionados: List<String>) {
        scope.launch {
            DesafioPreferences.saveDailyObjectives(context, idUsuario, seleccionados, diaActual)
            objetivosDia = seleccionados
            fechaDia = diaActual
            mostrarDialogoDia = false
        }
    }

    // Estado de desafíos
    val desafios by viewModel.desafios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.error.collectAsState()

    // Filtrado de desafíos (insensible a mayúsculas/minúsculas y espacios)
    fun normalizar(str: String) = str.trim().lowercase()
    val objetivosSemanaNorm = objetivosSemana?.map { normalizar(it) } ?: emptyList()
    val objetivosDiaNorm = objetivosDia?.map { normalizar(it) } ?: emptyList()

    val desafiosSemanales = desafios.filter {
        it.tipo == TipoDesafio.SEMANAL &&
        it.objetivosRelacionados.map { normalizar(it) }.any { obj -> obj in objetivosSemanaNorm }
    }
    val desafiosDiarios = desafios.filter {
        it.tipo == TipoDesafio.DIARIO &&
        it.objetivosRelacionados.map { normalizar(it) }.any { obj -> obj in objetivosDiaNorm }
    }

    // Diálogo de objetivos semanales
    if (mostrarDialogoSemana) {
        val seleccionados = remember { mutableStateListOf<String>() }
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 6.dp,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿Cuáles son tus objetivos para la semana?",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona uno o más para sugerirte desafíos semanales",
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
                    onClick = { if (seleccionados.isNotEmpty()) guardarObjetivosSemana(seleccionados) },
                    enabled = seleccionados.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    // Diálogo de objetivos diarios
    if (mostrarDialogoDia && !mostrarDialogoSemana) {
        val seleccionados = remember { mutableStateListOf<String>() }
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 6.dp,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿Cuáles son tus objetivos para hoy?",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona uno o más para sugerirte desafíos diarios",
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
                    onClick = { if (seleccionados.isNotEmpty()) guardarObjetivosDia(seleccionados) },
                    enabled = seleccionados.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Desafíos sugeridos",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF4CAF50)
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
                // Mostrar encabezado y lista de desafíos semanales
                if (desafiosSemanales.isNotEmpty()) {
                    item {
                        Text(
                            text = "Desafíos Semanales",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4CAF50)),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(desafiosSemanales) { desafio ->
                        DesafioCard(desafio)
                    }
                }
                // Mostrar encabezado y lista de desafíos diarios
                if (desafiosDiarios.isNotEmpty()) {
                    item {
                        Text(
                            text = "Desafíos Diarios",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4CAF50)),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(desafiosDiarios) { desafio ->
                        DesafioCard(desafio)
                    }
                }
                if (desafiosSemanales.isEmpty() && desafiosDiarios.isEmpty()) {
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

@Composable
fun DesafiosScreen() {
    DesafiosScreenTab()
} 
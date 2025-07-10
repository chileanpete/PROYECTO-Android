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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.R
import com.example.proyecto_droid.data.local.DesafioPreferences
import com.example.proyecto_droid.data.model.TipoDesafio
import com.example.proyecto_droid.ui.screens.desafios.components.DesafioCard
import com.example.proyecto_droid.ui.viewmodel.DesafiosViewModel
import kotlinx.coroutines.launch

@Composable
fun DesafiosScreenTab(
    viewModel: DesafiosViewModel = viewModel(),
    idUsuario: Int = 1
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarDesafios()
    }

    val objetivosDisponibles = listOf(
        stringResource(R.string.objetivo_salud_cardiovascular),
        stringResource(R.string.objetivo_perder_peso),
        stringResource(R.string.objetivo_fuerza_muscular),
        stringResource(R.string.objetivo_flexibilidad),
        stringResource(R.string.objetivo_reducir_estres),
        stringResource(R.string.objetivo_consistencia)
    )

    var objetivosSemana by remember { mutableStateOf<List<String>?>(null) }
    var fechaSemana by remember { mutableStateOf<String?>(null) }
    var objetivosDia by remember { mutableStateOf<List<String>?>(null) }
    var fechaDia by remember { mutableStateOf<String?>(null) }

    var mostrarDialogoSemana by remember { mutableStateOf(false) }
    var mostrarDialogoDia by remember { mutableStateOf(false) }

    val semanaActual = DesafioPreferences.getCurrentWeekStart()
    val diaActual = DesafioPreferences.getCurrentDay()

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

    fun guardarObjetivosSemana(seleccionados: List<String>) {
        scope.launch {
            DesafioPreferences.saveWeeklyObjectives(context, idUsuario, seleccionados, semanaActual)
            objetivosSemana = seleccionados
            fechaSemana = semanaActual
            mostrarDialogoSemana = false
        }
    }

    fun guardarObjetivosDia(seleccionados: List<String>) {
        scope.launch {
            DesafioPreferences.saveDailyObjectives(context, idUsuario, seleccionados, diaActual)
            objetivosDia = seleccionados
            fechaDia = diaActual
            mostrarDialogoDia = false
        }
    }

    val desafios by viewModel.desafios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.error.collectAsState()

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
                        text = stringResource(R.string.titulo_objetivos_semanales),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.subtitulo_objetivos_semanales),
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
                    Text(stringResource(R.string.boton_aceptar), color = Color.White)
                }
            }
        )
    }

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
                        text = stringResource(R.string.titulo_objetivos_diarios),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.subtitulo_objetivos_diarios),
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
                    Text(stringResource(R.string.boton_aceptar), color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_desafios_sugeridos),
            style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50))
            }
        } else if (errorMsg != null) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error al cargar desafíos",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                if (desafiosSemanales.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.titulo_desafios_semanales),
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4CAF50)),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(desafiosSemanales) { desafio ->
                        DesafioCard(desafio)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                if (desafiosDiarios.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.titulo_desafios_diarios),
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4CAF50)),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(desafiosDiarios) { desafio ->
                        DesafioCard(desafio)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                if (desafiosSemanales.isEmpty() && desafiosDiarios.isEmpty()) {
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No hay desafíos disponibles",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Selecciona tus objetivos para recibir desafíos personalizados",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }
                }
                
                // Espaciado adicional al final
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun DesafiosScreen() {
    DesafiosScreenTab()
}

package com.example.proyecto_droid.ui.screens.registro

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.TipoEjercicio
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.viewmodel.RegistroActividadViewModel
import com.example.proyecto_droid.viewmodel.RegistroActividadUiState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.app.Activity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.proyecto_droid.data.local.SessionManager
import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.compose.ui.res.stringResource
import com.example.proyecto_droid.R

fun formatearFecha(fechaIso: String?): String {
    if (fechaIso == null) return "-"
    return try {
        // Intenta parsear como LocalDateTime (con hora)
        val fecha = LocalDateTime.parse(fechaIso, DateTimeFormatter.ISO_DATE_TIME)
        fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        try {
            // Intenta parsear solo la fecha (sin hora)
            val fecha = java.time.LocalDate.parse(fechaIso, DateTimeFormatter.ISO_DATE)
            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e2: Exception) {
            // Si todo falla, intenta extraer los primeros 10 caracteres (yyyy-MM-dd)
            if (fechaIso.length >= 10) {
                val partes = fechaIso.substring(0, 10).split("-")
                if (partes.size == 3) {
                    return "${partes[2]}/${partes[1]}/${partes[0]}"
                }
            }
            "-"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadScreen(
    modifier: Modifier = Modifier,
    idUsuario: Int = 1 // TODO: Reemplaza por el id real del usuario logueado
) {
    val viewModel: RegistroActividadViewModel = viewModel()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var idUsuario by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(idUsuario) {
        if (idUsuario != null) {
            viewModel.cargarRegistros(idUsuario!!)
        }
    }
    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val deleteError by viewModel.deleteError.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val exportError by viewModel.exportError.collectAsState()
    val pdfFile by viewModel.pdfFile.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    // Eliminar permissionLauncher y requestPermissionAndExport

    // TODO: Cargar tipos de ejercicio reales si tienes endpoint, aquí simulado
    val tiposEjercicio = remember { 
        mutableStateListOf(
            TipoEjercicio(
                idTipoEjercicio = 1,
                nombre = "Cardio",
                descripcion = "Ejercicios cardiovasculares",
                categoria = "Aeróbico",
                intensidadRecomendada = 3
            ),
            TipoEjercicio(
                idTipoEjercicio = 2,
                nombre = "Pesas",
                descripcion = "Entrenamiento de fuerza",
                categoria = "Anaeróbico",
                intensidadRecomendada = 4
            ),
            TipoEjercicio(
                idTipoEjercicio = 3,
                nombre = "Yoga",
                descripcion = "Ejercicios de flexibilidad y equilibrio",
                categoria = "Flexibilidad",
                intensidadRecomendada = 2
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Header con título y botón de exportar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.titulo_registros_actividad),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = { viewModel.exportarPDF(context) },
                enabled = !isExporting
            ) {
                if (isExporting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = stringResource(R.string.exportar_pdf),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón para agregar registro
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isSaving
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.agregar_registro_actividad), color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botones para compartir y abrir PDF si existe
        if (pdfFile != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { com.example.proyecto_droid.util.FileUtils.sharePdf(context, pdfFile!!) },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.compartir_pdf), color = Color.White)
                }
                Button(
                    onClick = {
                        val uri: Uri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            pdfFile!!
                        )
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Abrir PDF"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.abrir_pdf), color = Color.White)
                }
            }
        }

        // Mostrar error de guardado si existe
        if (saveError != null) {
            AlertDialog(
                onDismissRequest = { viewModel.saveError.value = null },
                title = { Text("Error al guardar registro") },
                text = { Text(saveError ?: "") },
                confirmButton = {
                    TextButton(onClick = { viewModel.saveError.value = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Lista de registros
        when (val state = uiState) {
            is RegistroActividadUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            is RegistroActividadUiState.Error -> {
                val message = (state as RegistroActividadUiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = MaterialTheme.colorScheme.error)
                }
            }
            is RegistroActividadUiState.Success -> {
                val registros = (state as RegistroActividadUiState.Success).registros
                if (registros.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay registros de actividad física.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(registros) { registro ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Fecha: ${formatearFecha(registro.fecha_actividad)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tipo: ${registro.tipoEjercicio?.nombre ?: "-"}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Duración: ${registro.duracion_minutos ?: "-"} min",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Intensidad: ${registro.intensidad ?: "-"}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (!registro.comentario.isNullOrBlank()) {
                                        Text(
                                            text = "Comentario: ${registro.comentario}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddRegistroActividadDialog(
            tiposEjercicio = tiposEjercicio,
            onDismiss = { showAddDialog = false },
            onConfirm = { tipoEjercicio, duracion, intensidad, comentario ->
                Log.d("RegistroAPI", "onConfirm presionado")
                Log.d("RegistroAPI", "idUsuario: $idUsuario")
                val idUsuarioFinal = idUsuario ?: 1 // Fallback temporal para pruebas
                val fecha = "2024-01-15"
                val horaInicio = "07:00"
                val horaFin = "08:00"
                val caloriasQuemadas = duracion * 5 // Simulación
                viewModel.viewModelScope.launch {
                    Log.d("RegistroAPI", "Llamando a crearRegistroActividad")
                    viewModel.crearRegistroActividad(
                        idUsuario = idUsuarioFinal,
                        idTipoEjercicio = tipoEjercicio.idTipoEjercicio,
                        idRutina = null,
                        idRutinaEjercicio = null,
                        fecha = fecha,
                        horaInicio = horaInicio,
                        horaFin = horaFin,
                        duracion = duracion,
                        caloriasQuemadas = caloriasQuemadas,
                        intensidad = intensidad ?: tipoEjercicio.intensidadRecomendada,
                        comentario = comentario,
                        completada = true
                    )
                }
                showAddDialog = false
            }
        )
    }
} 

@Composable
fun AddRegistroActividadDialog(
    tiposEjercicio: List<TipoEjercicio>,
    onDismiss: () -> Unit,
    onConfirm: (TipoEjercicio, Int, Int?, String?) -> Unit
) {
    var selectedTipoEjercicio by remember { mutableStateOf<TipoEjercicio?>(null) }
    var duracion by remember { mutableStateOf("30") }
    var intensidad by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar Registro de Actividad",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Tipo de Ejercicio:", style = MaterialTheme.typography.labelMedium, color = GreenPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                tiposEjercicio.forEach { tipo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTipoEjercicio == tipo,
                            onClick = { selectedTipoEjercicio = tipo },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = GreenPrimary
                            )
                        )
                        Column {
                            Text(tipo.nombre, fontWeight = FontWeight.Medium)
                            Text(
                                tipo.descripcion ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración (minutos)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        focusedLabelColor = GreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = intensidad,
                    onValueChange = { intensidad = it },
                    label = { Text("Intensidad (opcional, número)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        focusedLabelColor = GreenPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        focusedLabelColor = GreenPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedTipoEjercicio?.let { tipo ->
                        onConfirm(
                            tipo,
                            duracion.toIntOrNull() ?: 30,
                            intensidad.toIntOrNull(),
                            comentario.takeIf { it.isNotBlank() }
                        )
                    }
                },
                enabled = selectedTipoEjercicio != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = GreenPrimary
                )
            ) {
                Text("Cancelar")
            }
        }
    )
} 
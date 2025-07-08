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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: RegistroActividadViewModel = viewModel()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val deleteError by viewModel.deleteError.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val exportError by viewModel.exportError.collectAsState()
    val pdfFile by viewModel.pdfFile.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    // Launcher para permisos de almacenamiento
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.exportarPDF()
        }
    }

    // Función para solicitar permisos y exportar
    fun requestPermissionAndExport() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.exportarPDF()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

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
                text = "Registros de Actividad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = { requestPermissionAndExport() },
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
                        contentDescription = "Exportar PDF",
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
            Text("Agregar Registro de Actividad", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mostrar error de guardado si existe
        if (saveError != null) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    TextButton(onClick = { /* Podrías limpiar el error aquí si lo deseas */ }) {
                        Text("Cerrar")
                    }
                }
            ) { Text(saveError ?: "") }
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
                            RegistroActividadCard(
                                registro = registro,
                                onEdit = null, // Eliminar
                                onDelete = {
                                    viewModel.confirmarEliminacion(registro.idActividad)
                                }
                            )
                        }
                    }
                }
            }
            is RegistroActividadUiState.DeleteConfirmation -> {
                AlertDialog(
                    onDismissRequest = { viewModel.limpiarMensajes() },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar este registro? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.eliminarRegistroActividad(state.id)
                            }
                        ) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.limpiarMensajes() }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
            is RegistroActividadUiState.DeleteSuccess -> {
                AlertDialog(
                    onDismissRequest = { viewModel.limpiarMensajes() },
                    title = { Text("Éxito") },
                    text = { Text(state.message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.limpiarMensajes() }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }

        // Mostrar error de eliminación si existe
        deleteError?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.limpiarMensajes() },
                title = { Text("Error") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { viewModel.limpiarMensajes() }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        // Mostrar error de exportación si existe
        exportError?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.limpiarErroresExportacion() },
                title = { Text("Error al exportar") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { viewModel.limpiarErroresExportacion() }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        // Mostrar mensaje de éxito de exportación y opciones
        if (!isExporting && pdfFile != null) {
            AlertDialog(
                onDismissRequest = { viewModel.limpiarPDF() },
                title = { Text("PDF Generado Exitosamente") },
                text = { 
                    Text("El PDF se ha guardado en tu dispositivo en la carpeta interna. ¿Qué te gustaría hacer?")
                },
                confirmButton = {
                    TextButton(onClick = { activity?.let { viewModel.compartirPDF(it) } }) {
                        Text("Compartir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.limpiarPDF() }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Mostrar mensaje de carga mientras se genera
        if (isExporting) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Generando PDF") },
                text = { Text("Por favor espera mientras se genera el reporte...") },
                confirmButton = { }
            )
        }
    }
    
    // Diálogo para agregar registro
    if (showAddDialog) {
        AddRegistroActividadDialog(
            tiposEjercicio = tiposEjercicio,
            onDismiss = { showAddDialog = false },
            onConfirm = { tipoEjercicio, duracion, intensidad, comentario ->
                // Fecha y hora actuales (puedes usar una librería para obtenerlas en producción)
                val fecha = "2024-01-15"
                val horaInicio = "07:00"
                val horaFin = "08:00"
                val caloriasQuemadas = duracion * 5 // Simulación
                viewModel.crearRegistroActividad(
                    idTipoEjercicio = tipoEjercicio.idTipoEjercicio,
                    idRutina = null,
                    fecha = fecha,
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    duracion = duracion,
                    caloriasQuemadas = caloriasQuemadas,
                    intensidad = intensidad ?: tipoEjercicio.intensidadRecomendada,
                    comentario = comentario,
                    completada = true
                )
                showAddDialog = false
            }
        )
    }

    // Snackbar para mostrar mensajes de éxito
    var showSuccessSnackbar by remember { mutableStateOf(false) }
    
    LaunchedEffect(pdfFile) {
        if (pdfFile != null) {
            showSuccessSnackbar = true
        }
    }
    
    if (showSuccessSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSuccessSnackbar = false }) {
                    Text("Cerrar")
                }
            }
        ) {
            Text("PDF descargado exitosamente en Downloads")
        }
    }
}

@Composable
fun RegistroActividadCard(
    registro: RegistroActividad,
    onEdit: (() -> Unit)?,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = registro.tipoEjercicio?.nombre ?: "Ejercicio no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Text(
                        text = "${registro.fechaActividad} - ${registro.horaInicio}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    onEdit?.let { edit ->
                        IconButton(onClick = edit) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = GreenPrimary)
                        }
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Duración: ${registro.duracionMinutos} min")
                Text("Calorías: ${registro.caloriasQuemadas}")
            }
            
            Text("Intensidad: ${registro.intensidad}")
            
            registro.comentario?.let { comentario ->
                if (comentario.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = comentario,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
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
                // Selector de tipo de ejercicio
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
                
                // Duración
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
                
                // Intensidad
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
                
                // Comentario
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
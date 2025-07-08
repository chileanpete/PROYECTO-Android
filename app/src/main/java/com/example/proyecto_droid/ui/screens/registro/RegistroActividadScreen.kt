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
                            // Aquí deberías tener un RegistroActividadCard o similar
                            Text(registro.toString())
                        }
                    }
                }
            }
        }
    }
} 
package com.example.proyecto_droid.ui.screens.registro

import android.app.Application
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.model.RegistroConsumo
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.viewmodel.RegistroConsumoViewModel
import com.example.proyecto_droid.viewmodel.RegistroConsumoUiState
import androidx.core.content.ContextCompat
import android.app.Activity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroConsumoScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: RegistroConsumoViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegistroConsumoViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val deleteError by viewModel.deleteError.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val exportError by viewModel.exportError.collectAsState()
    val pdfFile by viewModel.pdfFile.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var platos by remember { mutableStateOf<List<Plato>>(emptyList()) }
    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

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

    // Simulación: podrías cargar los platos desde la API si lo deseas
    LaunchedEffect(Unit) {
        platos = listOf(
            Plato(
                idPlato = 1,
                nombre = "Ensalada César",
                descripcion = "Ensalada fresca con lechuga, crutones y aderezo",
                precio = 8.50,
                caloriasPorPorcion = 250,
                proteinasG = 12.0,
                carbohidratosG = 15.0,
                grasasG = 18.0,
                esVegetariano = false,
                esVegano = false,
                sinGluten = false,
                imagenUrl = null,
                lugar = null,
                categoria = null
            ),
            Plato(
                idPlato = 2,
                nombre = "Pollo a la plancha",
                descripcion = "Pechuga de pollo a la plancha con vegetales",
                precio = 12.00,
                caloriasPorPorcion = 350,
                proteinasG = 35.0,
                carbohidratosG = 5.0,
                grasasG = 12.0,
                esVegetariano = false,
                esVegano = false,
                sinGluten = true,
                imagenUrl = null,
                lugar = null,
                categoria = null
            )
        )
    }

    if (showSnackBar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSnackBar = false }) {
                    Text("Cerrar")
                }
            }
        ) { Text(snackBarMessage) }
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
                text = "Registros de Consumo",
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
            Text("Agregar Registro de Comida", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de registros
        when (val state = uiState) {
            is RegistroConsumoUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            is RegistroConsumoUiState.Error -> {
                val message = (state as RegistroConsumoUiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = MaterialTheme.colorScheme.error)
                }
            }
            is RegistroConsumoUiState.Success -> {
                val registros = (state as RegistroConsumoUiState.Success).registros
                if (registros.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay registros de consumo.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(registros) { registro ->
                            // Aquí deberías tener un RegistroConsumoCard o similar
                            Text(registro.toString())
                        }
                    }
                }
            }
        }
    }
} 
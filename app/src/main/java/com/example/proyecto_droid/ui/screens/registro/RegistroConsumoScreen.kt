package com.example.proyecto_droid.ui.screens.registro

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.R
import com.example.proyecto_droid.data.model.RegistroConsumo
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.viewmodel.RegistroConsumoUiState
import com.example.proyecto_droid.viewmodel.RegistroConsumoViewModel

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
    val platos by viewModel.platos.collectAsState()
    val platosUiState by viewModel.platosUiState.collectAsState()
    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

    // Eliminar permissionLauncher y requestPermissionAndExport

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
                text = stringResource(R.string.titulo_registros_consumo),
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
            Text(stringResource(R.string.agregar_registro), color = Color.White)
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
                val message = state.message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = MaterialTheme.colorScheme.error)
                }
            }
            is RegistroConsumoUiState.Success -> {
                val registros = state.registros
                if (registros.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.sin_registros))
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(registros) { registro ->
                            RegistroConsumoCard(registro)
                        }
                    }
                }
            }
        }

        if (pdfFile != null) {
            Button(
                onClick = { viewModel.compartirPDF(context) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.compartir_pdf), color = Color.White)
            }
        }
    }

    if (isExporting) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenPrimary)
        }
    }

    if (!exportError.isNullOrBlank()) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { viewModel.limpiarErroresExportacion() }) {
                    Text(stringResource(R.string.cerrar))
                }
            }
        ) { Text(exportError ?: "") }
    }

    // Mostrar el diálogo para agregar registro de consumo
    if (showAddDialog) {
        AddRegistroConsumoDialog(
            platosUiState = platosUiState,
            onDismiss = { showAddDialog = false },
            onConfirm = { plato, porciones, valoracion, comentario ->
                // Aquí deberías obtener la fecha y hora actuales o pedirlas al usuario
                val fecha = "2024-01-15" // TODO: reemplazar por la fecha real
                val hora = "12:30" // TODO: reemplazar por la hora real
                viewModel.crearRegistroConsumo(
                    plato = plato,
                    porciones = porciones,
                    valoracion = valoracion,
                    comentario = comentario,
                    fecha = fecha,
                    hora = hora
                )
                showAddDialog = false
            },
            onRetryLoadPlatos = {
                viewModel.refreshPlatos()
            }
        )
    }
}

@Composable
fun RegistroConsumoCard(registro: RegistroConsumo) {
    val platoNombre = registro.plato?.nombre ?: "Plato"
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = GreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(platoNombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(registro.fechaConsumo.take(10), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(registro.horaConsumo?.take(5) ?: "N/A", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Porciones: ", fontWeight = FontWeight.SemiBold)
                Text(registro.porciones.toString())
                Spacer(modifier = Modifier.width(12.dp))
                Text("Calorías: ", fontWeight = FontWeight.SemiBold)
                Text(registro.caloriasTotales?.toString() ?: "N/A")
            }
            if (registro.valoracion != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Valoración: ${registro.valoracion}")
                }
            }
            if (!registro.comentario.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(registro.comentario ?: "")
                }
            }
        }
    }
}
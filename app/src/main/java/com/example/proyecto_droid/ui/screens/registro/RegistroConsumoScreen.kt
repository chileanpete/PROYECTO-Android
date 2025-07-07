package com.example.proyecto_droid.ui.screens.registro

import android.app.Application
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
    var showAddDialog by remember { mutableStateOf(false) }
    var platos by remember { mutableStateOf<List<Plato>>(emptyList()) }
    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

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
        // Header con estadísticas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Registro de Alimentación",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = GreenPrimary
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registra tus comidas y mantén un seguimiento de tu nutrición",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
        when (uiState) {
            is RegistroConsumoUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            is RegistroConsumoUiState.Error -> {
                val message = (uiState as RegistroConsumoUiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = MaterialTheme.colorScheme.error)
                }
            }
            is RegistroConsumoUiState.Success -> {
                val registros = (uiState as RegistroConsumoUiState.Success).registros
                if (registros.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay registros de alimentación.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(registros) { registro ->
                            RegistroConsumoCard(
                                registro = registro,
                                onEdit = { /* TODO: Implementar edición */ },
                                onDelete = { /* TODO: Implementar eliminación real */ }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Diálogo para agregar registro
    if (showAddDialog) {
        AddRegistroConsumoDialog(
            platos = platos,
            onDismiss = { showAddDialog = false },
            onConfirm = { plato, porciones, valoracion, comentario ->
                // Fecha y hora actuales (puedes usar una librería para obtenerlas en producción)
                val fecha = "2024-01-15"
                val hora = "12:30"
                viewModel.crearRegistroConsumo(
                    plato = plato,
                    porciones = porciones,
                    valoracion = valoracion,
                    comentario = comentario,
                    fecha = fecha,
                    hora = hora
                )
                showAddDialog = false
            }
        )
    }

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
    // Mostrar indicador de guardado
    if (isSaving) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenPrimary)
        }
    }
}

@Composable
fun RegistroConsumoCard(
    registro: RegistroConsumo,
    onEdit: () -> Unit,
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
                        text = registro.plato?.nombre ?: "Plato no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Text(
                        text = "${registro.fechaConsumo} - ${registro.horaConsumo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = GreenPrimary)
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
                Text("Porciones: ${registro.porciones}")
                Text("Calorías: ${registro.caloriasTotales}")
            }
            
            registro.valoracion?.let { valoracion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Valoración: ")
                    repeat(valoracion) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = GreenPrimary
                        )
                    }
                }
            }
            
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
fun AddRegistroConsumoDialog(
    platos: List<Plato>,
    onDismiss: () -> Unit,
    onConfirm: (Plato, Double, Int?, String?) -> Unit
) {
    var selectedPlato by remember { mutableStateOf<Plato?>(null) }
    var porciones by remember { mutableStateOf("1.0") }
    var valoracion by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Agregar Registro de Comida",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Selector de plato
                Text("Plato:", style = MaterialTheme.typography.labelMedium, color = GreenPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                
                platos.forEach { plato ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPlato == plato,
                            onClick = { selectedPlato = plato },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = GreenPrimary
                            )
                        )
                        Text(plato.nombre)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Porciones
                OutlinedTextField(
                    value = porciones,
                    onValueChange = { porciones = it },
                    label = { Text("Porciones") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        focusedLabelColor = GreenPrimary
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Valoración
                OutlinedTextField(
                    value = valoracion,
                    onValueChange = { valoracion = it },
                    label = { Text("Valoración (1-5)") },
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

                if (errorMsg != null) {
                    Text(
                        text = errorMsg ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val porcionesVal = porciones.toDoubleOrNull()
                    val valoracionVal = valoracion.toIntOrNull()
                    when {
                        selectedPlato == null -> errorMsg = "Debes seleccionar un plato"
                        porcionesVal == null || porcionesVal < 0.1 || porcionesVal > 10 -> errorMsg = "Porciones debe ser un número entre 0.1 y 10"
                        valoracion.isNotBlank() && (valoracionVal == null || valoracionVal < 1 || valoracionVal > 5) -> errorMsg = "Valoración debe ser un número entre 1 y 5"
                        else -> {
                            errorMsg = null
                            onConfirm(
                                selectedPlato!!,
                                porcionesVal ?: 1.0,
                                valoracionVal,
                                comentario.takeIf { it.isNotBlank() }
                            )
                        }
                    }
                },
                enabled = selectedPlato != null,
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
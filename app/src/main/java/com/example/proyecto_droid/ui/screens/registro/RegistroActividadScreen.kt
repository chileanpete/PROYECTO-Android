package com.example.proyecto_droid.ui.screens.registro

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadScreen(
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTipoEjercicio by remember { mutableStateOf<TipoEjercicio?>(null) }
    
    // Simular datos para demostración
    val registros = remember { mutableStateListOf<RegistroActividad>() }
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
                    text = "Registro de Actividad Física",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = GreenPrimary
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registra tus ejercicios y mantén un seguimiento de tu actividad física",
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
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Registro de Actividad", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de registros
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(registros) { registro ->
                RegistroActividadCard(
                    registro = registro,
                    onEdit = { /* TODO: Implementar edición */ },
                    onDelete = { registros.remove(registro) }
                )
            }
        }
    }
    
    // Diálogo para agregar registro
    if (showAddDialog) {
        AddRegistroActividadDialog(
            tiposEjercicio = tiposEjercicio,
            onDismiss = { showAddDialog = false },
            onConfirm = { tipoEjercicio, duracion, intensidad, comentario ->
                val nuevoRegistro = RegistroActividad(
                    idActividad = registros.size + 1,
                    idUsuario = 1,
                    idRutina = null,
                    idTipoEjercicio = tipoEjercicio.idTipoEjercicio,
                    fechaActividad = "2024-01-15",
                    horaInicio = "07:00",
                    horaFin = "07:00",
                    duracionMinutos = duracion,
                    caloriasQuemadas = duracion * 5, // Simulación
                    intensidad = intensidad ?: tipoEjercicio.intensidadRecomendada,
                    comentario = comentario,
                    puntosObtenidos = 15,
                    completada = true,
                    tipoEjercicio = tipoEjercicio,
                    rutinaEjercicio = null
                )
                registros.add(nuevoRegistro)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun RegistroActividadCard(
    registro: RegistroActividad,
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
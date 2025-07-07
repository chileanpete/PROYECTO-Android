package com.example.proyecto_droid.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.TipoEjercicio
import com.example.proyecto_droid.model.RutinaEjercicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadScreen(
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Simular datos para demostración
    val registros = remember { mutableStateListOf<RegistroActividad>() }
    val tiposEjercicio = remember { 
        mutableStateListOf(
            TipoEjercicio(
                idTipoEjercicio = 1,
                nombre = "Correr",
                descripcion = "Ejercicio cardiovascular",
                categoria = "Cardio",
                intensidadRecomendada = 4
            ),
            TipoEjercicio(
                idTipoEjercicio = 2,
                nombre = "Yoga",
                descripcion = "Ejercicio de flexibilidad y relajación",
                categoria = "Flexibilidad",
                intensidadRecomendada = 2
            )
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con estadísticas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Registro de Actividad Física",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registra tus ejercicios y actividades físicas",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón para agregar registro
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Actividad Física")
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
            onConfirm = { tipoEjercicio, horaInicio, horaFin, caloriasQuemadas, intensidad, comentario ->
                val nuevoRegistro = RegistroActividad(
                    idActividad = registros.size + 1,
                    idUsuario = 1,
                    idRutina = null,
                    idTipoEjercicio = tipoEjercicio?.idTipoEjercicio,
                    fechaActividad = "2024-01-15",
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    duracionMinutos = calcularDuracionMinutos(horaInicio, horaFin),
                    caloriasQuemadas = caloriasQuemadas,
                    intensidad = intensidad,
                    comentario = comentario,
                    puntosObtenidos = caloriasQuemadas / 10,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        text = registro.tipoEjercicio?.nombre ?: "Actividad física",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${registro.fechaActividad} - ${registro.horaInicio} a ${registro.horaFin}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Intensidad: ${registro.intensidad}/5")
                Text("Estado: ${if (registro.completada) "Completada" else "Pendiente"}")
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
fun AddRegistroActividadDialog(
    tiposEjercicio: List<TipoEjercicio>,
    onDismiss: () -> Unit,
    onConfirm: (TipoEjercicio?, String, String, Int, Int, String?) -> Unit
) {
    var selectedTipoEjercicio by remember { mutableStateOf<TipoEjercicio?>(null) }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var caloriasQuemadas by remember { mutableStateOf("") }
    var intensidad by remember { mutableStateOf("3") }
    var comentario by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Actividad Física") },
        text = {
            Column {
                // Tipo de ejercicio
                Text("Tipo de Ejercicio:", style = MaterialTheme.typography.labelMedium)
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
                            onClick = { selectedTipoEjercicio = tipo }
                        )
                        Text(tipo.nombre)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Hora inicio
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = { horaInicio = it },
                    label = { Text("Hora de inicio (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Hora fin
                OutlinedTextField(
                    value = horaFin,
                    onValueChange = { horaFin = it },
                    label = { Text("Hora de fin (HH:MM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Calorías quemadas
                OutlinedTextField(
                    value = caloriasQuemadas,
                    onValueChange = { caloriasQuemadas = it },
                    label = { Text("Calorías quemadas") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Intensidad
                OutlinedTextField(
                    value = intensidad,
                    onValueChange = { intensidad = it },
                    label = { Text("Intensidad (1-5)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Comentario
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedTipoEjercicio,
                        horaInicio,
                        horaFin,
                        caloriasQuemadas.toIntOrNull() ?: 0,
                        intensidad.toIntOrNull() ?: 3,
                        comentario.takeIf { it.isNotBlank() }
                    )
                },
                enabled = horaInicio.isNotBlank() && horaFin.isNotBlank() && caloriasQuemadas.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun calcularDuracionMinutos(horaInicio: String, horaFin: String): Int {
    return try {
        val partesInicio = horaInicio.split(":")
        val partesFin = horaFin.split(":")
        val inicioMinutos = partesInicio[0].toInt() * 60 + partesInicio[1].toInt()
        val finMinutos = partesFin[0].toInt() * 60 + partesFin[1].toInt()
        finMinutos - inicioMinutos
    } catch (e: Exception) {
        0
    }
} 
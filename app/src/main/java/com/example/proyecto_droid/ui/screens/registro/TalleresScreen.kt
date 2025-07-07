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
import com.example.proyecto_droid.model.TallerRecreativo
import com.example.proyecto_droid.model.InscripcionTaller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalleresScreen(
    modifier: Modifier = Modifier
) {
    var showInscripcionDialog by remember { mutableStateOf(false) }
    var selectedTaller by remember { mutableStateOf<TallerRecreativo?>(null) }
    
    // Simular datos para demostración
    val inscripciones = remember { mutableStateListOf<InscripcionTaller>() }
    val talleresDisponibles = remember { 
        mutableStateListOf(
            TallerRecreativo(
                idTaller = 1,
                nombre = "Yoga para Principiantes",
                descripcion = "Aprende las posturas básicas de yoga",
                instructor = "María González",
                categoria = "Bienestar",
                duracionMinutos = 60,
                nivelDificultad = 1,
                cupoMaximo = 20,
                costo = 15.0,
                ubicacion = "Gimnasio UCSC",
                fechaInicio = "2024-01-20T10:00:00",
                fechaFin = "2024-01-20T11:00:00",
                activo = true,
                imagenUrl = null,
                requisitos = "Traer mat de yoga"
            ),
            TallerRecreativo(
                idTaller = 2,
                nombre = "Cocina Saludable",
                descripcion = "Aprende a cocinar platos nutritivos",
                instructor = "Chef Carlos Ruiz",
                categoria = "Cocina",
                duracionMinutos = 90,
                nivelDificultad = 2,
                cupoMaximo = 15,
                costo = 25.0,
                ubicacion = "Cocina UCSC",
                fechaInicio = "2024-01-22T14:00:00",
                fechaFin = "2024-01-22T15:30:00",
                activo = true,
                imagenUrl = null,
                requisitos = "Traer delantal"
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
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Talleres Recreativos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Inscríbete en talleres y actividades recreativas",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sección de talleres disponibles
        Text(
            text = "Talleres Disponibles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(talleresDisponibles) { taller ->
                TallerCard(
                    taller = taller,
                    onInscribirse = {
                        selectedTaller = taller
                        showInscripcionDialog = true
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sección de mis inscripciones
        Text(
            text = "Mis Inscripciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(inscripciones) { inscripcion ->
                InscripcionTallerCard(
                    inscripcion = inscripcion,
                    onCompletar = {
                        // Simular completar taller
                        val index = inscripciones.indexOf(inscripcion)
                        if (index != -1) {
                            inscripciones[index] = inscripcion.copy(estado = "completado")
                        }
                    },
                    onCancelar = {
                        inscripciones.remove(inscripcion)
                    }
                )
            }
        }
    }
    
    // Diálogo para inscribirse
    if (showInscripcionDialog && selectedTaller != null) {
        InscripcionTallerDialog(
            taller = selectedTaller!!,
            onDismiss = { 
                showInscripcionDialog = false
                selectedTaller = null
            },
            onConfirm = { comentario ->
                val nuevaInscripcion = InscripcionTaller(
                    idInscripcion = inscripciones.size + 1,
                    idUsuario = 1,
                    idTaller = selectedTaller!!.idTaller,
                    fechaInscripcion = "2024-01-15T10:00:00",
                    estado = "inscrito",
                    puntosObtenidos = 20,
                    calificacion = null,
                    comentario = comentario,
                    taller = selectedTaller
                )
                inscripciones.add(nuevaInscripcion)
                showInscripcionDialog = false
                selectedTaller = null
            }
        )
    }
}

@Composable
fun TallerCard(
    taller: TallerRecreativo,
    onInscribirse: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = taller.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = taller.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Instructor: ${taller.instructor}")
                Text("Duración: ${taller.duracionMinutos} min")
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Categoría: ${taller.categoria}")
                Text("Costo: $${taller.costo}")
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ubicación: ${taller.ubicacion}")
                Text("Cupo: ${taller.cupoMaximo}")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onInscribirse,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Inscribirse")
            }
        }
    }
}

@Composable
fun InscripcionTallerCard(
    inscripcion: InscripcionTaller,
    onCompletar: () -> Unit,
    onCancelar: () -> Unit
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
                        text = inscripcion.taller?.nombre ?: "Taller no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estado: ${inscripcion.estado}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Row {
                    if (inscripcion.estado == "inscrito") {
                        IconButton(onClick = onCompletar) {
                            Icon(Icons.Default.Check, contentDescription = "Completar")
                        }
                    }
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.Default.Delete, contentDescription = "Cancelar")
                    }
                }
            }
            
            inscripcion.comentario?.let { comentario ->
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
fun InscripcionTallerDialog(
    taller: TallerRecreativo,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var comentario by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Inscribirse en ${taller.nombre}") },
        text = {
            Column {
                Text(
                    text = "¿Deseas inscribirte en este taller?",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(comentario.takeIf { it.isNotBlank() }) }) {
                Text("Inscribirse")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
} 
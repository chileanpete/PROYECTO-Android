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
import com.example.proyecto_droid.model.TallerRecreativo
import com.example.proyecto_droid.model.InscripcionTaller
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalleresScreen(
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTaller by remember { mutableStateOf<TallerRecreativo?>(null) }
    
    // Simular datos para demostración
    val inscripciones = remember { mutableStateListOf<InscripcionTaller>() }
    val talleres = remember { 
        mutableStateListOf(
            TallerRecreativo(
                idTaller = 1,
                nombre = "Yoga para Principiantes",
                descripcion = "Aprende las posturas básicas de yoga y técnicas de respiración",
                instructor = "María González",
                categoria = "Bienestar",
                duracionMinutos = 90,
                nivelDificultad = 1,
                cupoMaximo = 15,
                costo = 25.00,
                ubicacion = "Sala de Yoga - Piso 2",
                fechaInicio = "2024-01-20T09:00",
                fechaFin = "2024-01-20T10:30",
                activo = true,
                imagenUrl = null,
                requisitos = "Traer mat de yoga"
            ),
            TallerRecreativo(
                idTaller = 2,
                nombre = "Cocina Saludable",
                descripcion = "Aprende a preparar comidas nutritivas y deliciosas",
                instructor = "Chef Carlos Ruiz",
                categoria = "Nutrición",
                duracionMinutos = 120,
                nivelDificultad = 2,
                cupoMaximo = 12,
                costo = 35.00,
                ubicacion = "Cocina Experimental",
                fechaInicio = "2024-01-22T14:00",
                fechaFin = "2024-01-22T16:00",
                activo = true,
                imagenUrl = null,
                requisitos = "Traer delantal"
            ),
            TallerRecreativo(
                idTaller = 3,
                nombre = "Meditación Guiada",
                descripcion = "Sesión de meditación para reducir el estrés y mejorar la concentración",
                instructor = "Ana Martínez",
                categoria = "Bienestar",
                duracionMinutos = 60,
                nivelDificultad = 1,
                cupoMaximo = 20,
                costo = 15.00,
                ubicacion = "Sala de Meditación",
                fechaInicio = "2024-01-25T18:00",
                fechaFin = "2024-01-25T19:00",
                activo = true,
                imagenUrl = null,
                requisitos = null
            )
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Header con información
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
                    text = "Talleres Recreativos",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = GreenPrimary
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Participa en talleres para mejorar tu bienestar y aprender nuevas habilidades",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón para inscribirse
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
            Text("Inscribirse en Taller", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de talleres disponibles
        Text(
            text = "Talleres Disponibles",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(talleres) { taller ->
                TallerCard(
                    taller = taller,
                    onInscribirse = { /* TODO: Implementar inscripción */ }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mis inscripciones
        if (inscripciones.isNotEmpty()) {
            Text(
                text = "Mis Inscripciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(inscripciones) { inscripcion ->
                    InscripcionCard(
                        inscripcion = inscripcion,
                        onCancelar = { inscripciones.remove(inscripcion) }
                    )
                }
            }
        }
    }
    
    // Diálogo para inscribirse
    if (showAddDialog) {
        InscribirseTallerDialog(
            talleres = talleres,
            onDismiss = { showAddDialog = false },
            onConfirm = { taller ->
                val nuevaInscripcion = InscripcionTaller(
                    idInscripcion = inscripciones.size + 1,
                    idUsuario = 1,
                    idTaller = taller.idTaller,
                    fechaInscripcion = "2024-01-15",
                    estado = "Inscrito",
                    puntosObtenidos = 10,
                    calificacion = null,
                    comentario = null,
                    taller = taller
                )
                inscripciones.add(nuevaInscripcion)
                showAddDialog = false
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = taller.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = taller.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Inicio: ${taller.fechaInicio}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Fin: ${taller.fechaFin}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${taller.costo}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Text(
                        text = "Cupo: ${taller.cupoMaximo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instructor: ${taller.instructor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Button(
                    onClick = onInscribirse,
                    enabled = taller.activo,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (taller.activo) "Inscribirse" else "Inactivo",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun InscripcionCard(
    inscripcion: InscripcionTaller,
    onCancelar: () -> Unit
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
                        text = inscripcion.taller?.nombre ?: "Taller no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Text(
                        text = "Inicio: ${inscripcion.taller?.fechaInicio}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(onClick = onCancelar) {
                    Icon(Icons.Default.Delete, contentDescription = "Cancelar inscripción", tint = MaterialTheme.colorScheme.error)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Estado: ${inscripcion.estado}")
                Text("Inscrito: ${inscripcion.fechaInscripcion}")
            }
        }
    }
}

@Composable
fun InscribirseTallerDialog(
    talleres: List<TallerRecreativo>,
    onDismiss: () -> Unit,
    onConfirm: (TallerRecreativo) -> Unit
) {
    var selectedTaller by remember { mutableStateOf<TallerRecreativo?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Inscribirse en Taller",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Selecciona un taller:", style = MaterialTheme.typography.labelMedium, color = GreenPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                
                talleres.filter { it.activo }.forEach { taller ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTaller == taller,
                            onClick = { selectedTaller = taller },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = GreenPrimary
                            )
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(taller.nombre, fontWeight = FontWeight.Medium)
                            Text(
                                "Inicio: ${taller.fechaInicio}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Cupo: ${taller.cupoMaximo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedTaller?.let { taller ->
                        onConfirm(taller)
                    }
                },
                enabled = selectedTaller != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Inscribirse", color = Color.White)
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
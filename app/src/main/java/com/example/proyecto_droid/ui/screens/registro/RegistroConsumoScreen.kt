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
import com.example.proyecto_droid.model.RegistroConsumo
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.network.services.CrearRegistroConsumoRequest
import com.example.proyecto_droid.network.services.ActualizarRegistroConsumoRequest
import com.example.proyecto_droid.network.services.EstadisticasConsumo
import com.example.proyecto_droid.network.services.EstadisticasPeriodo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroConsumoScreen(
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedPlato by remember { mutableStateOf<Plato?>(null) }
    
    // Simular datos para demostración
    val registros = remember { mutableStateListOf<RegistroConsumo>() }
    val platos = remember { 
        mutableStateListOf(
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
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con estadísticas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Registro de Alimentación",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Registra tus comidas y mantén un seguimiento de tu nutrición",
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
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Registro de Comida")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de registros
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(registros) { registro ->
                RegistroConsumoCard(
                    registro = registro,
                    onEdit = { /* TODO: Implementar edición */ },
                    onDelete = { registros.remove(registro) }
                )
            }
        }
    }
    
    // Diálogo para agregar registro
    if (showAddDialog) {
        AddRegistroConsumoDialog(
            platos = platos,
            onDismiss = { showAddDialog = false },
            onConfirm = { plato, porciones, valoracion, comentario ->
                val nuevoRegistro = RegistroConsumo(
                    idConsumo = registros.size + 1,
                    idUsuario = 1,
                    idPlato = plato.idPlato,
                    fechaConsumo = "2024-01-15",
                    horaConsumo = "12:30",
                    porciones = porciones,
                    caloriasTotales = (plato.caloriasPorPorcion * porciones).toInt(),
                    valoracion = valoracion,
                    comentario = comentario,
                    puntosObtenidos = 10,
                    plato = plato
                )
                registros.add(nuevoRegistro)
                showAddDialog = false
            }
        )
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
                        text = registro.plato?.nombre ?: "Plato no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${registro.fechaConsumo} - ${registro.horaConsumo}",
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
                            tint = MaterialTheme.colorScheme.primary
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
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Registro de Comida") },
        text = {
            Column {
                // Selector de plato
                Text("Plato:", style = MaterialTheme.typography.labelMedium)
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
                            onClick = { selectedPlato = plato }
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
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Valoración
                OutlinedTextField(
                    value = valoracion,
                    onValueChange = { valoracion = it },
                    label = { Text("Valoración (1-5)") },
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
                    selectedPlato?.let { plato ->
                        onConfirm(
                            plato,
                            porciones.toDoubleOrNull() ?: 1.0,
                            valoracion.toIntOrNull(),
                            comentario.takeIf { it.isNotBlank() }
                        )
                    }
                },
                enabled = selectedPlato != null
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
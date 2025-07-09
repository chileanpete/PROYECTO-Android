package com.example.proyecto_droid.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRegistroConsumoDialog(
    platosUiState: com.example.proyecto_droid.viewmodel.PlatosUiState,
    onDismiss: () -> Unit,
    onConfirm: (Plato, Double, Int?, String?) -> Unit,
    onRetryLoadPlatos: () -> Unit = {}
) {
    var selectedPlato by remember { mutableStateOf<Plato?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var porciones by remember { mutableStateOf("1.0") }
    var valoracion by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Agregar Registro de Consumo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Selector de plato con manejo de estados
                Text("Plato", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                
                when (platosUiState) {
                    is com.example.proyecto_droid.viewmodel.PlatosUiState.Loading -> {
                        OutlinedTextField(
                            value = "Cargando platos...",
                            onValueChange = { },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        )
                    }
                    is com.example.proyecto_droid.viewmodel.PlatosUiState.Error -> {
                        Column {
                            OutlinedTextField(
                                value = "Error al cargar platos",
                                onValueChange = { },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                isError = true,
                                supportingText = { Text(platosUiState.message) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onRetryLoadPlatos,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                            ) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                    is com.example.proyecto_droid.viewmodel.PlatosUiState.Success -> {
                        val platos = platosUiState.platos
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedPlato?.nombre ?: "",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Selecciona un plato") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenPrimary,
                                    focusedLabelColor = GreenPrimary
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                platos.forEach { plato ->
                                    DropdownMenuItem(
                                        text = { Text(plato.nombre) },
                                        onClick = {
                                            selectedPlato = plato
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Porciones
                OutlinedTextField(
                    value = porciones,
                    onValueChange = { porciones = it },
                    label = { Text("Porciones") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
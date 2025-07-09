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
    platos: List<Plato>,
    onDismiss: () -> Unit,
    onConfirm: (Plato, Double, Int?, String?) -> Unit
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
                "Agregar Registro de Comida",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Selector de plato con dropdown
                Text("Plato:", style = MaterialTheme.typography.labelMedium, color = GreenPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPlato?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        placeholder = { Text("Selecciona un plato") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            focusedLabelColor = GreenPrimary
                        )
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
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
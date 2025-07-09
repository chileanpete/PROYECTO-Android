package com.example.proyecto_droid.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.ui.theme.GreenPrimary
import androidx.compose.ui.res.stringResource
import com.example.proyecto_droid.R
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRegistroConsumoDialog(
    platos: List<Plato>,
    onDismiss: () -> Unit,
    onConfirm: (Plato, Double, Int?, String?) -> Unit
) {
    val context = LocalContext.current
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
                stringResource(R.string.dialog_titulo_agregar_consumo),
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Selector de plato con dropdown
                Text(stringResource(R.string.dialog_label_plato), style = MaterialTheme.typography.labelMedium, color = GreenPrimary)
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
                        placeholder = { Text(stringResource(R.string.dialog_placeholder_selecciona_plato)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
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
                    label = { Text(stringResource(R.string.dialog_label_porciones)) },
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
                    label = { Text(stringResource(R.string.dialog_label_valoracion)) },
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
                    label = { Text(stringResource(R.string.dialog_label_comentario)) },
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
                        selectedPlato == null -> errorMsg = context.getString(R.string.dialog_error_sin_plato)
                        porcionesVal == null || porcionesVal < 0.1 || porcionesVal > 10 -> errorMsg = context.getString(R.string.dialog_error_porciones)
                        valoracion.isNotBlank() && (valoracionVal == null || valoracionVal < 1 || valoracionVal > 5) -> errorMsg = context.getString(R.string.dialog_error_valoracion)
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
                Text(stringResource(R.string.dialog_boton_guardar), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = GreenPrimary
                )
            ) {
                Text(stringResource(R.string.dialog_boton_cancelar))
            }
        }
    )
} 
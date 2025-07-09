package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.R
import com.example.proyecto_droid.data.model.User
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.viewmodel.ProfileViewModel
import com.example.proyecto_droid.ui.viewmodel.ProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreferencesScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = uiState.currentUser
    
    // Efecto para manejar el éxito de la actualización
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            // Esperar 2 segundos y luego navegar de vuelta
            kotlinx.coroutines.delay(2000)
            onSaveClick()
        }
    }
    
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var nivelActividad by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var preferenciasAlimentarias by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    
    // Valores originales para comparación
    var originalPeso by remember { mutableStateOf("") }
    var originalAltura by remember { mutableStateOf("") }
    var originalNivelActividad by remember { mutableStateOf("") }
    var originalObjetivo by remember { mutableStateOf("") }
    var originalPreferenciasAlimentarias by remember { mutableStateOf("") }
    var originalAlergias by remember { mutableStateOf("") }
    
    // Actualizar valores cuando currentUser cambie
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            // Actualizar valores actuales
            peso = user.pesoKg?.toInt()?.toString() ?: ""
            altura = user.alturaCm?.toString() ?: ""
            nivelActividad = user.nivelActividad ?: ""
            objetivo = user.objetivoPrincipal ?: ""
            preferenciasAlimentarias = user.preferenciasAlimentarias ?: ""
            alergias = user.alergias ?: ""
            
            // Actualizar valores originales para comparación
            originalPeso = user.pesoKg?.toInt()?.toString() ?: ""
            originalAltura = user.alturaCm?.toString() ?: ""
            originalNivelActividad = user.nivelActividad ?: ""
            originalObjetivo = user.objetivoPrincipal ?: ""
            originalPreferenciasAlimentarias = user.preferenciasAlimentarias ?: ""
            originalAlergias = user.alergias ?: ""
        }
    }
    
    // Función para verificar si al menos un campo ha cambiado
    val hasChanges = remember(peso, altura, nivelActividad, objetivo, preferenciasAlimentarias, alergias) {
        peso != originalPeso || 
        altura != originalAltura || 
        nivelActividad != originalNivelActividad || 
        objetivo != originalObjetivo || 
        preferenciasAlimentarias != originalPreferenciasAlimentarias || 
        alergias != originalAlergias
    }
    
    var expandedPeso by remember { mutableStateOf(false) }
    var expandedAltura by remember { mutableStateOf(false) }
    var expandedNivel by remember { mutableStateOf(false) }
    var expandedObjetivo by remember { mutableStateOf(false) }
    var expandedPreferencias by remember { mutableStateOf(false) }
    var expandedAlergias by remember { mutableStateOf(false) }
    
    // Opciones para peso (35-150 kg en incrementos de 5)
    val opcionesPeso = (35..150 step 5).map { it.toString() }
    
    // Opciones para altura (140-210 cm en incrementos de 5)
    val opcionesAltura = (140..210 step 5).map { it.toString() }
    
    val nivelesActividad = listOf(
        "sedentario" to "Sedentario",
        "ligero" to "Ligero", 
        "moderado" to "Moderado",
        "activo" to "Activo",
        "muy_activo" to "Muy Activo"
    )
    
    val objetivos = listOf(
        "perder_peso" to "Perder Peso",
        "mantener_peso" to "Mantener Peso",
        "ganar_peso" to "Ganar Peso",
        "ganar_musculo" to "Ganar Músculo"
    )
    
    val opcionesPreferenciasAlimentarias = listOf(
        "ninguna" to "Ninguna en particular",
        "vegetariana" to "Vegetariana",
        "vegana" to "Vegana",
        "sin_gluten" to "Sin gluten",
        "sin_lactosa" to "Sin lactosa",
        "paleo" to "Paleo",
        "keto" to "Keto",
        "mediterranea" to "Mediterránea",
        "baja_carbohidratos" to "Baja en carbohidratos",
        "baja_grasas" to "Baja en grasas"
    )
    
    val opcionesAlergias = listOf(
        "ninguna" to "Ninguna",
        "frutos_secos" to "Frutos secos",
        "mariscos" to "Mariscos",
        "huevos" to "Huevos",
        "lacteos" to "Lácteos",
        "soja" to "Soja",
        "gluten" to "Gluten",
        "pescado" to "Pescado",
        "sesamo" to "Sésamo",
        "mostaza" to "Mostaza",
        "apio" to "Apio",
        "multiples" to "Múltiples alergias"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Barra superior
        TopAppBar(
            title = { 
                Text(
                    text = "Editar Preferencias",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Regresar",
                        tint = GreenPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Peso
            Text(
                text = "Peso (kg)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedPeso,
                onExpandedChange = { expandedPeso = !expandedPeso },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (peso.isNotBlank()) "$peso kg" else "Seleccionar peso",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.pesoKg?.toInt()?.let { "$it kg" } ?: "Seleccionar peso",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPeso) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedPeso,
                    onDismissRequest = { expandedPeso = false }
                ) {
                    opcionesPeso.forEach { pesoOption ->
                        DropdownMenuItem(
                            text = { Text("$pesoOption kg") },
                            onClick = {
                                peso = pesoOption
                                expandedPeso = false
                            }
                        )
                    }
                }
            }
            
            // Altura
            Text(
                text = "Altura (cm)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedAltura,
                onExpandedChange = { expandedAltura = !expandedAltura },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = if (altura.isNotBlank()) "$altura cm" else "Seleccionar altura",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.alturaCm?.let { "$it cm" } ?: "Seleccionar altura",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAltura) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedAltura,
                    onDismissRequest = { expandedAltura = false }
                ) {
                    opcionesAltura.forEach { alturaOption ->
                        DropdownMenuItem(
                            text = { Text("$alturaOption cm") },
                            onClick = {
                                altura = alturaOption
                                expandedAltura = false
                            }
                        )
                    }
                }
            }
            
            // Nivel de Actividad
            Text(
                text = "Nivel de Actividad",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedNivel,
                onExpandedChange = { expandedNivel = !expandedNivel },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nivelesActividad.find { it.first == nivelActividad }?.second ?: "Seleccionar nivel",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.nivelActividad?.let { 
                                nivelesActividad.find { nivel -> nivel.first == it }?.second 
                            } ?: "Seleccionar nivel",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNivel) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedNivel,
                    onDismissRequest = { expandedNivel = false }
                ) {
                    nivelesActividad.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                nivelActividad = value
                                expandedNivel = false
                            }
                        )
                    }
                }
            }
            
            // Objetivo Principal
            Text(
                text = "Objetivo Principal",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedObjetivo,
                onExpandedChange = { expandedObjetivo = !expandedObjetivo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = objetivos.find { it.first == objetivo }?.second ?: "Seleccionar objetivo",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.objetivoPrincipal?.let { 
                                objetivos.find { obj -> obj.first == it }?.second 
                            } ?: "Seleccionar objetivo",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedObjetivo) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedObjetivo,
                    onDismissRequest = { expandedObjetivo = false }
                ) {
                    objetivos.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                objetivo = value
                                expandedObjetivo = false
                            }
                        )
                    }
                }
            }
            
            // Preferencias Alimentarias
            Text(
                text = "Preferencias Alimentarias",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedPreferencias,
                onExpandedChange = { expandedPreferencias = !expandedPreferencias },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = opcionesPreferenciasAlimentarias.find { it.first == preferenciasAlimentarias }?.second ?: "Seleccionar preferencia",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.preferenciasAlimentarias?.let { 
                                opcionesPreferenciasAlimentarias.find { pref -> pref.first == it }?.second 
                            } ?: "Seleccionar preferencia",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPreferencias) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedPreferencias,
                    onDismissRequest = { expandedPreferencias = false }
                ) {
                    opcionesPreferenciasAlimentarias.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                preferenciasAlimentarias = value
                                expandedPreferencias = false
                            }
                        )
                    }
                }
            }
            
            // Alergias
            Text(
                text = "Alergias",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expandedAlergias,
                onExpandedChange = { expandedAlergias = !expandedAlergias },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = opcionesAlergias.find { it.first == alergias }?.second ?: "Seleccionar alergia",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { 
                        Text(
                            text = currentUser?.alergias?.let { 
                                opcionesAlergias.find { alergia -> alergia.first == it }?.second 
                            } ?: "Seleccionar alergia",
                            color = Color.Gray
                        ) 
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAlergias) },
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expandedAlergias,
                    onDismissRequest = { expandedAlergias = false }
                ) {
                    opcionesAlergias.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                alergias = value
                                expandedAlergias = false
                            }
                        )
                    }
                }
            }
            
            // Mostrar mensaje de estado
            if (!hasChanges) {
                Text(
                    text = "Modifica al menos un campo para guardar cambios",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            if (uiState.updateSuccess && uiState.successMessage != null) {
                Text(
                    text = uiState.successMessage!!,
                    color = GreenPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Botón Guardar
            Button(
                onClick = {
                    if (currentUser != null) {
                        // Debug: verificar valores antes de enviar
                        println("DEBUG: nivelActividad = '$nivelActividad'")
                        println("DEBUG: objetivo = '$objetivo'")
                        println("DEBUG: currentUser.nivelActividad = '${currentUser?.nivelActividad}'")
                        println("DEBUG: currentUser.objetivoPrincipal = '${currentUser?.objetivoPrincipal}'")
                        
                        // Limpiar estado previo
                        viewModel.handleEvent(ProfileEvent.ClearSuccess)
                        
                        // Asegurar que siempre tengamos valores válidos
                        val finalNivelActividad = if (nivelActividad.isNotBlank()) {
                            nivelActividad
                        } else {
                            currentUser.nivelActividad ?: "sedentario" // Valor por defecto
                        }
                        
                        val finalObjetivo = if (objetivo.isNotBlank()) {
                            objetivo  
                        } else {
                            currentUser.objetivoPrincipal ?: "mantener_peso" // Valor por defecto
                        }
                        
                        val updatedUser = currentUser.copy(
                            pesoKg = peso.toDoubleOrNull() ?: currentUser.pesoKg,
                            alturaCm = altura.toIntOrNull() ?: currentUser.alturaCm,
                            nivelActividad = finalNivelActividad,
                            objetivoPrincipal = finalObjetivo,
                            preferenciasAlimentarias = if (preferenciasAlimentarias.isNotBlank()) preferenciasAlimentarias else currentUser.preferenciasAlimentarias,
                            alergias = if (alergias.isNotBlank()) alergias else currentUser.alergias
                        )
                        
                        println("DEBUG: Usuario actualizado - nivelActividad = '${updatedUser.nivelActividad}', objetivoPrincipal = '${updatedUser.objetivoPrincipal}'")
                        viewModel.handleEvent(ProfileEvent.UpdateProfile(updatedUser))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                enabled = !uiState.isLoading && hasChanges
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Guardar",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditPreferencesScreenPreview() {
    EditPreferencesScreen(
        onBackClick = {},
        onSaveClick = {}
    )
} 
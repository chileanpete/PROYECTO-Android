package com.example.proyecto_droid.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.viewmodel.RegisterEvent
import com.example.proyecto_droid.ui.viewmodel.RegisterViewModel
import com.example.proyecto_droid.ui.viewmodel.SessionEvent
import com.example.proyecto_droid.ui.viewmodel.SessionViewModel
import com.example.proyecto_droid.data.model.User
import java.util.*

@Composable
fun RegisterScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Observar cambios en el estado de registro
    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            android.widget.Toast.makeText(
                context, 
                "¡Registro exitoso! Bienvenido ${uiState.nombre} ${uiState.apellidos}", 
                android.widget.Toast.LENGTH_LONG
            ).show()
            
            // Crear el usuario registrado y actualizar la sesión
            val registeredUser = User(
                email = uiState.email,
                passwordHash = uiState.password,
                nombre = uiState.nombre,
                apellidos = uiState.apellidos,
                fechaNacimiento = uiState.fechaNacimiento,
                genero = uiState.genero,
                alturaCm = viewModel.extractNumber(uiState.alturaCm),
                pesoKg = viewModel.extractNumber(uiState.pesoKg).toDouble(),
                nivelActividad = uiState.nivelActividad,
                objetivoPrincipal = uiState.objetivoPrincipal,
                preferenciasAlimentarias = null,
                alergias = null
            )
            
            // Actualizar la sesión con el usuario registrado
            sessionViewModel.handleEvent(SessionEvent.Login(registeredUser))
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = maxWidth
        val horizontalPadding = if (maxWidth < 400.dp) 16.dp else 32.dp
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // === HEADER: Título y barra de progreso ===
            Text(
                text = "Registro - Paso ${uiState.currentStep} de 3",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 28.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Indicador de progreso
            LinearProgressIndicator(
                progress = { uiState.currentStep / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(bottom = 24.dp),
                color = GreenPrimary,
            )

            // === CONTENIDO POR PASOS ===
            when (uiState.currentStep) {
                1 -> RegisterStep1(viewModel, uiState)
                2 -> RegisterStep2(viewModel, uiState)
                3 -> RegisterStep3(viewModel, uiState)
            }
            
            // === MENSAJES DE ERROR ===
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === BOTONES DE NAVEGACIÓN ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Anterior/Cancelar
                if (uiState.currentStep > 1) {
                    Button(
                        onClick = { viewModel.handleEvent(RegisterEvent.PreviousStep) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Anterior", color = GreenPrimary, fontSize = 16.sp)
                    }
                } else {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Cancelar", color = GreenPrimary, fontSize = 16.sp)
                    }
                }
                
                // Botón Siguiente/Completar Registro
                Button(
                    onClick = {
                        if (uiState.currentStep == 3) {
                            viewModel.handleEvent(RegisterEvent.RegisterUser)
                        } else {
                            viewModel.handleEvent(RegisterEvent.NextStep)
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White, 
                            strokeWidth = 2.dp, 
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = if (uiState.currentStep == 3) "Completar Registro" else "Siguiente",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ============================================================================
// PASO 1: Información básica (Correo, Nombre, Apellido, Contraseña, Confirmar contraseña)
// ============================================================================
@Composable
private fun RegisterStep1(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    Text(
        text = "Información básica",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
    // Correo electrónico
    OutlinedTextField(
        value = uiState.email,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateEmail(it)) },
        label = { Text("Correo electrónico *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    // Nombre
    OutlinedTextField(
        value = uiState.nombre,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateNombre(it)) },
        label = { Text("Nombre *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    // Apellidos
    OutlinedTextField(
        value = uiState.apellidos,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateApellidos(it)) },
        label = { Text("Apellidos *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    // Contraseña
    OutlinedTextField(
        value = uiState.password,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdatePassword(it)) },
        label = { Text("Contraseña *") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    // Confirmar contraseña
    OutlinedTextField(
        value = uiState.confirmPassword,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateConfirmPassword(it)) },
        label = { Text("Confirmar contraseña *") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
}

// ============================================================================
// PASO 2: Información personal (Fecha nacimiento, Género, Altura, Peso)
// ============================================================================
@Composable
private fun RegisterStep2(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    val context = LocalContext.current
    
    Text(
        text = "Información personal",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
    // === FECHA DE NACIMIENTO con DatePicker ===
    val dateInteractionSource = remember { MutableInteractionSource() }
    val isDatePressed by dateInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isDatePressed) {
        if (isDatePressed) {
            // Crear DatePicker al hacer clic
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            
            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    viewModel.handleEvent(RegisterEvent.UpdateFechaNacimiento(date))
                },
                year, month, day
            ).show()
        }
    }
    
    OutlinedTextField(
        value = uiState.fechaNacimiento,
        onValueChange = { },
        readOnly = true,
        label = { Text("Fecha de nacimiento *") },
        trailingIcon = { 
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha"
            )
        },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        ),
        interactionSource = dateInteractionSource
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    // === GÉNERO (Dropdown) ===
    var generoExpanded by remember { mutableStateOf(false) }
    val generoInteractionSource = remember { MutableInteractionSource() }
    val isGeneroPressed by generoInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isGeneroPressed) {
        if (isGeneroPressed) {
            generoExpanded = !generoExpanded
        }
    }
    
    BoxWithConstraints {
        val textFieldWidth = maxWidth
        
        OutlinedTextField(
            value = formatOptionText(uiState.genero),
            onValueChange = { },
            readOnly = true,
            label = { Text("Género *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (generoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (generoExpanded) "Cerrar" else "Abrir"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            ),
            interactionSource = generoInteractionSource
        )
        DropdownMenu(
            expanded = generoExpanded,
            onDismissRequest = { generoExpanded = false },
            modifier = Modifier
                .width(textFieldWidth)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            uiState.generos.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = formatOptionText(option),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (option == uiState.genero) GreenPrimary else Color.Black
                        )
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateGenero(option))
                        generoExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = if (option == uiState.genero) GreenPrimary else Color.Black
                    )
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    // === ALTURA (Dropdown 100-250 cm) ===
    var alturaExpanded by remember { mutableStateOf(false) }
    val alturaInteractionSource = remember { MutableInteractionSource() }
    val isAlturaPressed by alturaInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isAlturaPressed) {
        if (isAlturaPressed) {
            alturaExpanded = !alturaExpanded
        }
    }
    
    BoxWithConstraints {
        val textFieldWidth = maxWidth
        
        OutlinedTextField(
            value = uiState.alturaCm,
            onValueChange = { },
            readOnly = true,
            label = { Text("Altura *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (alturaExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (alturaExpanded) "Cerrar" else "Abrir"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            ),
            interactionSource = alturaInteractionSource
        )
        DropdownMenu(
            expanded = alturaExpanded,
            onDismissRequest = { alturaExpanded = false },
            modifier = Modifier
                .width(textFieldWidth)
                .height(200.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            uiState.alturas.forEach { altura ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = altura,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (altura == uiState.alturaCm) GreenPrimary else Color.Black
                        )
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateAltura(altura))
                        alturaExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = if (altura == uiState.alturaCm) GreenPrimary else Color.Black
                    )
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    // === PESO (Dropdown 30-200 kg) ===
    var pesoExpanded by remember { mutableStateOf(false) }
    val pesoInteractionSource = remember { MutableInteractionSource() }
    val isPesoPressed by pesoInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isPesoPressed) {
        if (isPesoPressed) {
            pesoExpanded = !pesoExpanded
        }
    }
    
    BoxWithConstraints {
        val textFieldWidth = maxWidth
        
        OutlinedTextField(
            value = uiState.pesoKg,
            onValueChange = { },
            readOnly = true,
            label = { Text("Peso *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (pesoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (pesoExpanded) "Cerrar" else "Abrir"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            ),
            interactionSource = pesoInteractionSource
        )
        DropdownMenu(
            expanded = pesoExpanded,
            onDismissRequest = { pesoExpanded = false },
            modifier = Modifier
                .width(textFieldWidth)
                .height(200.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            uiState.pesos.forEach { peso ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = peso,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (peso == uiState.pesoKg) GreenPrimary else Color.Black
                        )
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdatePeso(peso))
                        pesoExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = if (peso == uiState.pesoKg) GreenPrimary else Color.Black
                    )
                )
            }
        }
    }
}

// ============================================================================
// PASO 3: Preferencias (Nivel de actividad física, Objetivo principal)
// ============================================================================
@Composable
private fun RegisterStep3(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    Text(
        text = "Actividad física y objetivos",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
    // === NIVEL DE ACTIVIDAD FÍSICA (Dropdown) ===
    var nivelActividadExpanded by remember { mutableStateOf(false) }
    val nivelActividadInteractionSource = remember { MutableInteractionSource() }
    val isNivelActividadPressed by nivelActividadInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isNivelActividadPressed) {
        if (isNivelActividadPressed) {
            nivelActividadExpanded = !nivelActividadExpanded
        }
    }
    
    BoxWithConstraints {
        val textFieldWidth = maxWidth
        
        OutlinedTextField(
            value = formatOptionText(uiState.nivelActividad),
            onValueChange = { },
            readOnly = true,
            label = { Text("Nivel de actividad física *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (nivelActividadExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (nivelActividadExpanded) "Cerrar" else "Abrir"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            ),
            interactionSource = nivelActividadInteractionSource
        )
        DropdownMenu(
            expanded = nivelActividadExpanded,
            onDismissRequest = { nivelActividadExpanded = false },
            modifier = Modifier
                .width(textFieldWidth)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            uiState.nivelesActividad.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = formatOptionText(option),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (option == uiState.nivelActividad) GreenPrimary else Color.Black
                        )
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateNivelActividad(option))
                        nivelActividadExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = if (option == uiState.nivelActividad) GreenPrimary else Color.Black
                    )
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    // === OBJETIVO PRINCIPAL (Dropdown) ===
    var objetivoExpanded by remember { mutableStateOf(false) }
    val objetivoInteractionSource = remember { MutableInteractionSource() }
    val isObjetivoPressed by objetivoInteractionSource.collectIsPressedAsState()
    
    LaunchedEffect(isObjetivoPressed) {
        if (isObjetivoPressed) {
            objetivoExpanded = !objetivoExpanded
        }
    }
    
    BoxWithConstraints {
        val textFieldWidth = maxWidth
        
        OutlinedTextField(
            value = formatOptionText(uiState.objetivoPrincipal),
            onValueChange = { },
            readOnly = true,
            label = { Text("Objetivo principal *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (objetivoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (objetivoExpanded) "Cerrar" else "Abrir"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            ),
            interactionSource = objetivoInteractionSource
        )
        DropdownMenu(
            expanded = objetivoExpanded,
            onDismissRequest = { objetivoExpanded = false },
            modifier = Modifier
                .width(textFieldWidth)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            uiState.objetivos.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = formatOptionText(option),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (option == uiState.objetivoPrincipal) GreenPrimary else Color.Black
                        )
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateObjetivoPrincipal(option))
                        objetivoExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = if (option == uiState.objetivoPrincipal) GreenPrimary else Color.Black
                    )
                )
            }
        }
    }
}

// Función para formatear opciones con guiones bajos
private fun formatOptionText(option: String): String {
    return when (option) {
        // Niveles de actividad
        "sedentario" -> "Sedentario"
        "ligero" -> "Ligero"
        "moderado" -> "Moderado"
        "activo" -> "Activo"
        "muy_activo" -> "Muy activo"
        
        // Objetivos
        "perder_peso" -> "Perder peso"
        "mantener_peso" -> "Mantener peso"
        "ganar_peso" -> "Ganar peso"
        "ganar_musculo" -> "Ganar músculo"
        
        // Géneros
        "M" -> "Masculino"
        "F" -> "Femenino"
        "O" -> "Otro"
        
        else -> option.replace("_", " ").replaceFirstChar { it.uppercase() }
    }
} 
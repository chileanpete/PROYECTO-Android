package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                alturaCm = uiState.alturaCm.toIntOrNull() ?: 0,
                pesoKg = uiState.pesoKg.toDoubleOrNull() ?: 0.0,
                nivelActividad = uiState.nivelActividad,
                objetivoPrincipal = uiState.objetivoPrincipal,
                preferenciasAlimentarias = uiState.preferenciasAlimentarias,
                alergias = uiState.alergias
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
            
            // Título y progreso
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

            when (uiState.currentStep) {
                1 -> RegisterStep1(viewModel, uiState)
                2 -> RegisterStep2(viewModel, uiState)
                3 -> RegisterStep3(viewModel, uiState)
            }
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            if (uiState.currentStep == 3) "Completar Registro" else "Siguiente",
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

@Composable
private fun RegisterStep1(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    Text(
        text = "Información básica",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
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
    Spacer(modifier = Modifier.height(12.dp))
    
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
}

@Composable
private fun RegisterStep2(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    Text(
        text = "Información personal",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
    OutlinedTextField(
        value = uiState.fechaNacimiento,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateFechaNacimiento(it)) },
        label = { Text("Fecha de nacimiento (YYYY-MM-DD) *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    var generoExpanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = when(uiState.genero) {
                "M" -> "Masculino"
                "F" -> "Femenino"
                "O" -> "Otro"
                else -> ""
            },
            onValueChange = { },
            readOnly = true,
            label = { Text("Género *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (generoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (generoExpanded) "Cerrar" else "Abrir",
                    modifier = Modifier.clickable { generoExpanded = !generoExpanded }
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            )
        )
        DropdownMenu(
            expanded = generoExpanded,
            onDismissRequest = { generoExpanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(IntrinsicSize.Min)
        ) {
            uiState.generos.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(when(option) {
                            "M" -> "Masculino"
                            "F" -> "Femenino"
                            "O" -> "Otro"
                            else -> option
                        })
                    },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateGenero(option))
                        generoExpanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = uiState.alturaCm,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateAltura(it)) },
        label = { Text("Altura (cm) *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = uiState.pesoKg,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdatePeso(it)) },
        label = { Text("Peso (kg) *") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
}

@Composable
private fun RegisterStep3(viewModel: RegisterViewModel, uiState: com.example.proyecto_droid.ui.viewmodel.RegisterUiState) {
    Text(
        text = "Preferencias y objetivos",
        color = LightGrayText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 24.dp)
    )
    
    var nivelActividadExpanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = uiState.nivelActividad,
            onValueChange = { },
            readOnly = true,
            label = { Text("Nivel de actividad física *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (nivelActividadExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (nivelActividadExpanded) "Cerrar" else "Abrir",
                    modifier = Modifier.clickable { nivelActividadExpanded = !nivelActividadExpanded }
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            )
        )
        DropdownMenu(
            expanded = nivelActividadExpanded,
            onDismissRequest = { nivelActividadExpanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(IntrinsicSize.Min)
        ) {
            uiState.nivelesActividad.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateNivelActividad(option))
                        nivelActividadExpanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    var objetivoExpanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = uiState.objetivoPrincipal,
            onValueChange = { },
            readOnly = true,
            label = { Text("Objetivo principal *") },
            trailingIcon = { 
                Icon(
                    imageVector = if (objetivoExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (objetivoExpanded) "Cerrar" else "Abrir",
                    modifier = Modifier.clickable { objetivoExpanded = !objetivoExpanded }
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText
            )
        )
        DropdownMenu(
            expanded = objetivoExpanded,
            onDismissRequest = { objetivoExpanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(IntrinsicSize.Min)
        ) {
            uiState.objetivos.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { 
                        viewModel.handleEvent(RegisterEvent.UpdateObjetivoPrincipal(option))
                        objetivoExpanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = uiState.preferenciasAlimentarias,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdatePreferenciasAlimentarias(it)) },
        label = { Text("Preferencias alimentarias (opcional)") },
        minLines = 3,
        maxLines = 5,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    
    OutlinedTextField(
        value = uiState.alergias,
        onValueChange = { viewModel.handleEvent(RegisterEvent.UpdateAlergias(it)) },
        label = { Text("Alergias (opcional)") },
        minLines = 2,
        maxLines = 4,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = LightGrayText
        )
    )
} 
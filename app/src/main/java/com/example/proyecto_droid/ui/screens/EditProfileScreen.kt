package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = uiState.currentUser
    
    var name by remember { mutableStateOf(currentUser?.nombre ?: "") }
    var lastname by remember { mutableStateOf(currentUser?.apellidos ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    
    // Valores originales para comparación
    val originalName = currentUser?.nombre ?: ""
    val originalLastname = currentUser?.apellidos ?: ""
    val originalEmail = currentUser?.email ?: ""
    
    // Función para verificar si al menos un campo ha cambiado
    val hasChanges = remember(name, lastname, email) {
        name.trim() != originalName || 
        lastname.trim() != originalLastname || 
        email.trim() != originalEmail
    }
    
    // Función para verificar que los campos no estén vacíos
    val areFieldsValid = remember(name, lastname, email) {
        name.trim().isNotBlank() && 
        lastname.trim().isNotBlank() && 
        email.trim().isNotBlank()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Barra superior
        TopAppBar(
            title = { 
                Text(
                    text = "Editar Perfil",
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
            
            // Campo Nombre
            Text(
                text = "Nombre",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text(
                        text = originalName.ifBlank { "Ingresa tu nombre" },
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            
            // Campo Apellido
            Text(
                text = "Apellidos",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text(
                        text = originalLastname.ifBlank { "Ingresa tus apellidos" },
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            
            // Campo Email
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text(
                        text = originalEmail.ifBlank { "Ingresa tu email" },
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            
            // Mostrar mensaje de estado
            if (!hasChanges && areFieldsValid) {
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón Guardar
            Button(
                onClick = {
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(
                            nombre = name.trim(),
                            apellidos = lastname.trim(),
                            email = email.trim()
                        )
                        viewModel.handleEvent(ProfileEvent.UpdateProfile(updatedUser))
                        onSaveClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary
                ),
                enabled = !uiState.isLoading && areFieldsValid && hasChanges
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
fun EditProfileScreenPreview() {
    EditProfileScreen(
        onBackClick = {},
        onSaveClick = {}
    )
} 
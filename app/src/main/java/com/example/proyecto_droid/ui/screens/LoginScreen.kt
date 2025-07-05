package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.proyecto_droid.R
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.viewmodel.LoginEvent
import com.example.proyecto_droid.ui.viewmodel.LoginViewModel
import com.example.proyecto_droid.ui.viewmodel.SessionEvent
import com.example.proyecto_droid.ui.viewmodel.SessionViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Observar cambios en el estado de login
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            android.widget.Toast.makeText(context, "¡Login exitoso!", android.widget.Toast.LENGTH_SHORT).show()
            uiState.currentUser?.let { user ->
                sessionViewModel.handleEvent(SessionEvent.Login(user))
            }
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
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Vida Sana UCSC",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Tu vida saludable universitaria",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.handleEvent(LoginEvent.UpdateEmail(it)) },
                label = { Text("Ingrese su correo") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = GreenPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.handleEvent(LoginEvent.UpdatePassword(it)) },
                label = { Text("Ingrese su contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = GreenPrimary)
                },
                trailingIcon = {
                    val image = if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { viewModel.handleEvent(LoginEvent.TogglePasswordVisibility) }) {
                        Icon(image, contentDescription = if (uiState.passwordVisible) "Ocultar" else "Mostrar")
                    }
                },
                singleLine = true,
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = GreenPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable {
                        navController.navigate("forgot")
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            Button(
                onClick = { viewModel.handleEvent(LoginEvent.Login) },
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                } else {
                    Text("Iniciar Sesión", color = Color.White, fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SocialIconButton(
                    iconRes = R.drawable.ic_google,
                    contentDescription = "Google",
                    onClick = {
                        android.widget.Toast.makeText(context, "Registro con Google (simulado)", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.width(24.dp))
                SocialIconButton(
                    iconRes = R.drawable.ic_facebook,
                    contentDescription = "Facebook",
                    onClick = {
                        android.widget.Toast.makeText(context, "Registro con Facebook (simulado)", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tienes cuenta? ", color = LightGrayText)
                Text(
                    text = "Regístrate",
                    color = GreenPrimary,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
            
            // Botón temporal para desarrollo - limpiar BD
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.clearDatabase()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
            ) {
                Text("Limpiar BD (Desarrollo)", color = Color.White, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SocialIconButton(iconRes: Int, contentDescription: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }
} 
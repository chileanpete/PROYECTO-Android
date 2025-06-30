package com.example.proyecto_droid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.theme.BackgroundLight
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.proyecto_droid.ui.theme.Proyecto_droidTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.BoxWithConstraints

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_droidTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    AppNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        composable("main") { MainAppScreen(navController) }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    fun validate(): Boolean {
        return email == "usuario@ucsc.cl" && password == "usuario123"
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo o usuario") },
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = GreenPrimary)
                },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    loading = true
                    error = null
                    if (!validate()) {
                        error = "Credenciales incorrectas. Usa: usuario@ucsc.cl / usuario123"
                        loading = false
                    } else {
                        coroutineScope.launch {
                            delay(1500)
                            loading = false
                            Toast.makeText(context, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                enabled = !loading,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (loading) {
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
                        Toast.makeText(context, "Registro con Google (simulado)", Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.width(24.dp))
                SocialIconButton(
                    iconRes = R.drawable.ic_facebook,
                    contentDescription = "Facebook",
                    onClick = {
                        Toast.makeText(context, "Registro con Facebook (simulado)", Toast.LENGTH_SHORT).show()
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
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    fun validate(): Boolean {
        return email.isNotBlank() && password.length >= 6 && password == confirmPassword
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
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Crea tu cuenta para Vida Sana UCSC",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (!validate()) {
                    error = "Verifica los campos."
                } else {
                    Toast.makeText(context, "¡Registro exitoso! (simulado)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Registrarse", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Volver", color = GreenPrimary, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    fun validate(): Boolean {
        return email.isNotBlank()
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
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Te enviaremos un correo para restablecer tu contraseña",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (!validate()) {
                    error = "Introduce tu correo."
                } else {
                    Toast.makeText(context, "¡Correo enviado! (simulado)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Enviar", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Volver", color = GreenPrimary, fontSize = 18.sp)
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

@Composable
fun MainAppScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Vida Sana UCSC",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "¡Bienvenido a tu app de vida saludable!",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Cerrar Sesión", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
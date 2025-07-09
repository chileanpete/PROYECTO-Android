package com.example.proyecto_droid.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.proyecto_droid.R
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.viewmodel.ProfileEvent
import com.example.proyecto_droid.ui.viewmodel.ProfileViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToEditPreferences: () -> Unit = {},
    onNavigateToChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Estados para la imagen de perfil y permisos
    var profileImageUri by remember { mutableStateOf<String?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    
    // Función para verificar permisos
    val checkPermission = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    // Función para guardar imagen en almacenamiento interno
    val saveImageToInternalStorage = { uri: Uri ->
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            // Guardar la ruta en SharedPreferences
            val sharedPref = context.getSharedPreferences("profile_prefs", android.content.Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("profile_image_uri", file.absolutePath)
                apply()
            }
            
            // Actualizar el estado local
            profileImageUri = file.absolutePath
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            saveImageToInternalStorage(selectedUri)
        }
    }
    
    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            showPermissionDialog = true
        }
    }
    
    // Verificar permisos al iniciar
    LaunchedEffect(Unit) {
        hasPermission = checkPermission()
    }
    
    // Manejar navegación
    LaunchedEffect(uiState) {
        if (uiState.shouldNavigateToEditProfile) {
            onNavigateToEditProfile()
            viewModel.clearNavigationState()
        } else if (uiState.shouldNavigateToEditPreferences) {
            onNavigateToEditPreferences()
            viewModel.clearNavigationState()
        } else if (uiState.shouldNavigateToChangePassword) {
            onNavigateToChangePassword()
            viewModel.clearNavigationState()
        } else if (!uiState.isLoggedIn && !uiState.isLoading) {
            onLogout()
        }
    }
    
    // Cargar imagen de perfil guardada localmente
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("profile_prefs", android.content.Context.MODE_PRIVATE)
        profileImageUri = sharedPref.getString("profile_image_uri", null)
    }
    
    // Refrescar perfil cuando vuelve de otras pantallas
    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(ProfileEvent.RefreshProfile)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color(0xFFF8F8F8))
    ) {
        when {
            uiState.isLoading -> {
                // Mostrar indicador de carga
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            
            uiState.error != null -> {
                // Mostrar mensaje de error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { viewModel.handleEvent(ProfileEvent.RefreshProfile) },
                            colors = CardDefaults.cardColors(containerColor = GreenPrimary)
                        ) {
                            Text(
                                text = "Reintentar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                                    }
        }
    }
    
    // Diálogo de permisos
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { 
                Text(
                    text = "Permisos necesarios",
                    style = MaterialTheme.typography.headlineSmall
                ) 
            },
            text = { 
                Text(
                    text = "Para cambiar tu foto de perfil, necesitamos acceso a tus imágenes. Por favor, permite el acceso en la configuración de la aplicación.",
                    style = MaterialTheme.typography.bodyMedium
                ) 
            },
            confirmButton = {
                Button(
                    onClick = { showPermissionDialog = false }
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}
            
            uiState.currentUser != null -> {
                val user = uiState.currentUser!!
                
                // Sección superior con fondo blanco
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    color = androidx.compose.ui.graphics.Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mi Perfil",
                            style = MaterialTheme.typography.headlineSmall,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(30.dp))
                        
                        // Foto de perfil circular con badge de edición
                        Box(
                            modifier = Modifier.size(120.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                shadowElevation = 8.dp,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (profileImageUri != null) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(profileImageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Foto de perfil",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_profile),
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            
                            // Badge de edición
                            Surface(
                                shape = CircleShape,
                                color = GreenPrimary,
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-4).dp, y = (-4).dp)
                                    .clickable { 
                                        if (hasPermission) {
                                            imagePickerLauncher.launch("image/*")
                                        } else {
                                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                Manifest.permission.READ_MEDIA_IMAGES
                                            } else {
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            }
                                            permissionLauncher.launch(permission)
                                        }
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_add_post),
                                        contentDescription = "Editar foto",
                                        tint = androidx.compose.ui.graphics.Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = user.getFullName(),
                            style = MaterialTheme.typography.titleLarge,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 22.sp
                        )
                        
                        Text(
                            text = user.getDisplayObjetivo(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightGrayText,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                // Opciones de perfil
                ProfileMenuCard(
                    icon = R.drawable.ic_profile,
                    text = "Editar Perfil",
                    onClick = { viewModel.handleEvent(ProfileEvent.NavigateToEditProfile) }
                )
                
                ProfileMenuCard(
                    icon = R.drawable.ic_settings,
                    text = "Editar Preferencias",
                    onClick = { viewModel.handleEvent(ProfileEvent.NavigateToEditPreferences) }
                )
                
                ProfileMenuCard(
                    icon = R.drawable.ic_lock,
                    text = "Cambiar Contraseña",
                    onClick = { viewModel.handleEvent(ProfileEvent.NavigateToChangePassword) }
                )
                
                ProfileMenuCard(
                    icon = R.drawable.ic_logout,
                    text = "Cerrar Sesión",
                    onClick = { viewModel.handleEvent(ProfileEvent.Logout) },
                    isLogout = true
                )
            }
            
            else -> {
                // Usuario no autenticado
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Usuario no autenticado",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMenuCard(
    icon: Int,
    text: String,
    onClick: () -> Unit = {},
    isLogout: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(18.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isLogout) androidx.compose.ui.graphics.Color.Red.copy(alpha = 0.1f) else GreenPrimary.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = text,
                        tint = if (isLogout) androidx.compose.ui.graphics.Color.Red else GreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(18.dp))
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isLogout) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Black
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (!isLogout) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(180f)
                )
            }
        }
    }
} 
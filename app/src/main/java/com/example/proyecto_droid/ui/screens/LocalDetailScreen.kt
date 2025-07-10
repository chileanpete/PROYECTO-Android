package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.data.model.LugarComida
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalDetailScreen(
    navController: NavController,
    lugar: LugarComida,
    platosViewModel: PlatosViewModel = viewModel(
        factory = PlatosViewModelFactory(LocalContext.current)
    ),
    favoritosMenuViewModel: FavoritosMenuViewModel = viewModel(
        factory = FavoritosMenuViewModelFactory(LocalContext.current)
    ),
    sessionViewModel: SessionViewModel = viewModel()
) {
    val platosState by platosViewModel.uiState.collectAsStateWithLifecycle()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    val isAddingToMenu by favoritosMenuViewModel.isAddingToMenu.collectAsStateWithLifecycle()
    
    val userId = sessionState.currentUser?.id ?: 0

    // Cargar platos del local
    LaunchedEffect(lugar.id) {
        platosViewModel.getPlatosByPlace(lugar.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header con información del local
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GreenPrimary, GreenPrimary.copy(alpha = 0.8f))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Botón de regreso
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = "Local",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Información del local
                Column {
                    Text(
                        text = lugar.nombre,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (!lugar.direccion.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Dirección",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lugar.direccion,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (!lugar.telefono.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lugar.telefono,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                    
                    if (!lugar.horarioApertura.isNullOrBlank() && !lugar.horarioCierre.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = "Horario",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${lugar.horarioApertura} - ${lugar.horarioCierre}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }
                }
            }
        }

        // Contenido de platos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Título de sección
            Text(
                text = "Platos Disponibles",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GreenPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lista de platos
            when (val currentState = platosState) {
                is PlatosUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = GreenPrimary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando platos...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                is PlatosUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error al cargar platos",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = currentState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { platosViewModel.getPlatosByPlace(lugar.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                
                is PlatosUiState.Success -> {
                    if (currentState.platos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Restaurant,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No hay platos disponibles",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Este local aún no tiene platos registrados o no están disponibles en este momento.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { platosViewModel.getPlatosByPlace(lugar.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Actualizar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Actualizar")
                                }
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(currentState.platos) { plato ->
                                LocalPlatoCard(
                                    plato = plato,
                                    onAddToFavoritos = {
                                        favoritosMenuViewModel.addToFavoritos(userId, plato.id)
                                    },
                                    onAddToMenu = { tipoComida ->
                                        favoritosMenuViewModel.addPlatoToMenu(
                                            userId, plato.id, tipoComida
                                        )
                                    },
                                    isAddingToMenu = isAddingToMenu
                                )
                            }
                        }
                    }
                }
                
                else -> {}
            }
        }
    }
}

@Composable
fun LocalPlatoCard(
    plato: Plato,
    onAddToFavoritos: () -> Unit,
    onAddToMenu: (String) -> Unit,
    isAddingToMenu: Boolean,
    modifier: Modifier = Modifier
) {
    var showMenuDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Nombre del plato
            Text(
                text = plato.nombre,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = GreenPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Descripción (si existe)
            if (!plato.descripcion.isNullOrBlank()) {
                Text(
                    text = plato.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Información básica
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!plato.caloriasPorPorcion.isNullOrBlank()) {
                    Text(
                        text = "${plato.caloriasPorPorcion} cal",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                if (!plato.precio.isNullOrBlank()) {
                    Text(
                        text = "$${plato.precio}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = GreenPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón favoritos
                Button(
                    onClick = onAddToFavoritos,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                // Botón menú
                Button(
                    onClick = { showMenuDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = !isAddingToMenu,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Al menú",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
    
    // Dialog para seleccionar tipo de comida
    if (showMenuDialog) {
        AlertDialog(
            onDismissRequest = { showMenuDialog = false },
            title = { Text("Añadir al menú") },
            text = { Text("¿Para qué momento del día?") },
            confirmButton = {
                Column {
                    val tiposComida = listOf("desayuno", "almuerzo", "cena", "snack")
                    tiposComida.forEach { tipo ->
                        TextButton(
                            onClick = {
                                onAddToMenu(tipo)
                                showMenuDialog = false
                            }
                        ) {
                            Text(tipo.replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showMenuDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
} 
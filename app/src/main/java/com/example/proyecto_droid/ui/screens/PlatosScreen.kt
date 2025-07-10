package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.data.model.*
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.viewmodel.*
import java.time.format.DateTimeFormatter
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatosScreen(
    favoritosMenuViewModel: FavoritosMenuViewModel = viewModel(
        factory = FavoritosMenuViewModelFactory(LocalContext.current)
    ),
    sessionViewModel: SessionViewModel = viewModel()
) {
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    val favoritosState by favoritosMenuViewModel.favoritosUiState.collectAsStateWithLifecycle()
    val menuState by favoritosMenuViewModel.menuUiState.collectAsStateWithLifecycle()
    val recomendacionesState by favoritosMenuViewModel.recomendacionesUiState.collectAsStateWithLifecycle()
    val selectedDate by favoritosMenuViewModel.selectedDate.collectAsStateWithLifecycle()
    val isAddingToMenu by favoritosMenuViewModel.isAddingToMenu.collectAsStateWithLifecycle()
    
    val userId = sessionState.currentUser?.id ?: 0
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Menú Diario", "Menú Favorito")

    // Cargar datos iniciales
    LaunchedEffect(userId) {
        if (userId > 0) {
            favoritosMenuViewModel.loadFavoritos(userId)
            favoritosMenuViewModel.loadMenusForDate(userId)
            favoritosMenuViewModel.generateDailyRecommendations()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp)
    ) {
        // Header con fecha seleccionada
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gestión de Menús",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fecha: ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                    Row {
                        IconButton(
                            onClick = { 
                                favoritosMenuViewModel.setSelectedDate(selectedDate.minusDays(1))
                                favoritosMenuViewModel.loadMenusForDate(userId, selectedDate.minusDays(1))
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Día anterior",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = { 
                                favoritosMenuViewModel.setSelectedDate(selectedDate.plusDays(1))
                                favoritosMenuViewModel.loadMenusForDate(userId, selectedDate.plusDays(1))
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "Día siguiente",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pestañas
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = GreenPrimary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 3.dp,
                        color = GreenPrimary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.padding(vertical = 12.dp),
                    selectedContentColor = GreenPrimary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido según la pestaña seleccionada
        when (selectedTab) {
            0 -> {
                // Pestaña de Menú Diario (Recomendaciones automáticas)
                Column {
                    // Header de recomendaciones
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Recomendaciones del Día",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "3 comidas seleccionadas para ti",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            Button(
                                onClick = { favoritosMenuViewModel.generateDailyRecommendations() },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                modifier = Modifier.size(width = 120.dp, height = 36.dp),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Generar nuevas",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Nuevas",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Contenido de recomendaciones
                    when (val currentState = recomendacionesState) {
                        is RecomendacionesUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = GreenPrimary)
                            }
                        }
                        is RecomendacionesUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Error: ${currentState.message}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { favoritosMenuViewModel.generateDailyRecommendations() },
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                        is RecomendacionesUiState.Success -> {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(currentState.recomendaciones.take(3)) { plato ->
                                    RecommendationCard(
                                        plato = plato,
                                        onAcceptRecommendation = { tipoComida ->
                                            favoritosMenuViewModel.acceptRecommendation(userId, plato, tipoComida)
                                        },
                                        isAddingToMenu = isAddingToMenu
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                    
                    // Spacer entre recomendaciones y menús actuales
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sección de menús actuales del día
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Tu menú de hoy",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GreenPrimary
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Contenido de menús según el estado
                            when (val currentMenuState = menuState) {
                                is MenuUiState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = GreenPrimary)
                                    }
                                }
                                is MenuUiState.Error -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            Icons.Default.Error,
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Error al cargar menús",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = currentMenuState.message,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = { favoritosMenuViewModel.retryLoadMenus(userId) },
                                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                                        ) {
                                            Text("Reintentar")
                                        }
                                    }
                                }
                                is MenuUiState.Success -> {
                                    if (currentMenuState.menus.isEmpty()) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                Icons.Default.Restaurant,
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = Color.Gray
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "No hay platos en tu menú de hoy",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = "Agrega recomendaciones o favoritos a tu menú",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        MenuList(
                                            menus = currentMenuState.menus,
                                            onRemoveFromMenu = { menuId ->
                                                favoritosMenuViewModel.removePlatoFromMenu(menuId, userId)
                                            },
                                            onMarkCompleted = { menuId ->
                                                favoritosMenuViewModel.marcarMenuCompletado(menuId, userId)
                                            }
                                        )
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
            1 -> {
                // Pestaña de Menú Favorito
                when (val currentState = favoritosState) {
                    is FavoritosUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }
                    is FavoritosUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${currentState.message}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { favoritosMenuViewModel.retryLoadFavoritos(userId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    is FavoritosUiState.Success -> {
                        if (currentState.menusFavoritos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No tienes menús favoritos",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Crea menús desde la pestaña de Locales",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(currentState.menusFavoritos) { menuFavorito ->
                                    MenuFavoritoCard(
                                        menuFavorito = menuFavorito,
                                        onUsarMenu = {
                                            favoritosMenuViewModel.usarMenuFavorito(menuFavorito.id, userId)
                                        },
                                        onEliminarMenu = {
                                            favoritosMenuViewModel.removeFromFavoritos(menuFavorito.id, userId)
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
}

@Composable
fun MenuFavoritoCard(
    menuFavorito: MenuFavoritoUsuario,
    onUsarMenu: () -> Unit,
    onEliminarMenu: () -> Unit,
    isAddingToMenu: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header del menú
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = menuFavorito.nombreMenu,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GreenPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Tipo de comida y estadísticas
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (menuFavorito.tipoComida != null) {
                            Surface(
                                color = GreenPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = menuFavorito.tipoComida.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GreenPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        if (menuFavorito.caloriasTotales != null) {
                            Text(
                                text = "${menuFavorito.caloriasTotales.toInt()} cal",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                // Botón para expandir/contraer
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Contraer" else "Expandir",
                        tint = GreenPrimary
                    )
                }
            }
            
            // Descripción si existe
            if (!menuFavorito.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = menuFavorito.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Lista de platos (cuando está expandido)
            if (expanded && menuFavorito.platos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Platos incluidos:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = GreenPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                menuFavorito.platos.take(5).forEach { menuPlato ->
                    menuPlato.plato?.let { plato ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "• ${plato.nombre}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${menuPlato.cantidad} ${menuPlato.unidad}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                if (menuFavorito.platos.size > 5) {
                    Text(
                        text = "... y ${menuFavorito.platos.size - 5} más",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Estadísticas de uso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${menuFavorito.platos.size} platos • Usado ${menuFavorito.vecesUsado} veces",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para usar el menú
                Button(
                    onClick = onUsarMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = !isAddingToMenu,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.RestaurantMenu,
                        contentDescription = "Usar menú",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Usar")
                }
                
                // Botón para eliminar
                OutlinedButton(
                    onClick = onEliminarMenu,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar menú",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun MenuList(
    menus: List<MenuDiario>,
    onRemoveFromMenu: (Int) -> Unit,
    onMarkCompleted: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedMenus = menus.groupBy { it.tipoComida }
    
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        groupedMenus.forEach { (tipoComida, menusDelTipo) ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = tipoComida.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = GreenPrimary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        menusDelTipo.forEach { menu ->
                            MenuItemCard(
                                menu = menu,
                                onRemoveFromMenu = { onRemoveFromMenu(menu.id) },
                                onMarkCompleted = { onMarkCompleted(menu.id) }
                            )
                            
                            if (menu != menusDelTipo.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(
    menu: MenuDiario,
    onRemoveFromMenu: () -> Unit,
    onMarkCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (menu.completado) 
                GreenPrimary.copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                menu.plato?.let { plato ->
                    Text(
                        text = plato.nombre,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (menu.completado) Color.Gray else MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "${menu.cantidad} ${menu.unidad}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            Row {
                if (!menu.completado) {
                    IconButton(
                        onClick = onMarkCompleted,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Marcar completado",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Completado",
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                IconButton(
                    onClick = onRemoveFromMenu,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Quitar del menú",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(
    plato: Plato,
    onAcceptRecommendation: (String) -> Unit,
    isAddingToMenu: Boolean,
    modifier: Modifier = Modifier
) {
    var showMenuDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con badge de recomendación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plato.nombre,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = plato.descripcion ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Surface(
                    color = GreenPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Recomendado",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Información nutricional (si está disponible)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Calorías",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "${plato.caloriasPorPorcion ?: "N/A"} kcal",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                
                Column {
                    Text(
                        text = "Precio",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${plato.precio ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                
                Column {
                    Text(
                        text = "Categoría",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = plato.categoria?.nombre ?: "Sin categoría",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showMenuDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = !isAddingToMenu,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isAddingToMenu) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aceptar recomendación",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aceptar")
                    }
                }
                
                OutlinedButton(
                    onClick = { /* TODO: Agregar a favoritos desde recomendaciones */ },
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        modifier = Modifier.size(20.dp),
                        tint = GreenPrimary
                    )
                }
            }
        }
    }
    
    // Dialog para seleccionar tipo de comida
    if (showMenuDialog) {
        AlertDialog(
            onDismissRequest = { showMenuDialog = false },
            title = { 
                Text(
                    text = "Añadir al menú",
                    style = MaterialTheme.typography.titleMedium
                ) 
            },
            text = { 
                Text(
                    text = "¿Para qué momento del día deseas esta recomendación?",
                    style = MaterialTheme.typography.bodyMedium
                ) 
            },
            confirmButton = {
                Column {
                    val tiposComida = listOf(
                        "desayuno" to "🌅 Desayuno",
                        "almuerzo" to "☀️ Almuerzo", 
                        "cena" to "🌙 Cena",
                        "snack" to "🍿 Snack"
                    )
                    tiposComida.forEach { (tipo, label) ->
                        TextButton(
                            onClick = {
                                onAcceptRecommendation(tipo)
                                showMenuDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Start
                            )
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
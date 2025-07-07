package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.ui.screens.desafios.DesafiosScreen
import com.example.proyecto_droid.ui.screens.registro.RegistroConsumoScreen
import com.example.proyecto_droid.ui.screens.registro.RegistroActividadScreen
import com.example.proyecto_droid.ui.screens.registro.TalleresScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroActividadesScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        TabItem("Desafíos", Icons.Default.EmojiEvents),
        TabItem("Alimentación", Icons.Default.Restaurant),
        TabItem("Actividad", Icons.Default.DirectionsRun),
        TabItem("Talleres", Icons.Default.School)
    )
    
    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar con navegación
        TopAppBar(
            title = { Text("Registro de Actividades") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(tab.title) },
                    icon = { Icon(tab.icon, contentDescription = tab.title) }
                )
            }
        }
        
        // Contenido de las pestañas
        when (selectedTab) {
            0 -> DesafiosScreen()
            1 -> RegistroConsumoScreen()
            2 -> RegistroActividadScreen()
            3 -> TalleresScreen()
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector
) 
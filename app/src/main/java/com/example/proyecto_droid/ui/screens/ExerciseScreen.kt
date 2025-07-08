package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.proyecto_droid.ui.screens.desafios.DesafiosScreen
import com.example.proyecto_droid.ui.screens.registro.RegistroActividadScreen
import com.example.proyecto_droid.ui.screens.registro.RegistroConsumoScreen

@Composable
fun ExerciseScreen() {
    val tabs = listOf("Desafíos", "Actividades", "Alimentacion")
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTab) {
            0 -> DesafiosScreen()
            1 -> RegistroActividadScreen()
            2 -> RegistroConsumoScreen()
        }
    }
} 
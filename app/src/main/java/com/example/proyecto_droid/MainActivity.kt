package com.example.proyecto_droid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_droid.Proyecto_droid
import com.example.proyecto_droid.ui.AppNavigation
import com.example.proyecto_droid.ui.theme.MainScreen
import com.example.proyecto_droid.ui.screens.platos.LocalAddPlatoScreen
import com.example.proyecto_droid.ui.screens.lugares.LocalAddLugarScreen
import com.example.proyecto_droid.ui.screens.categorias.AddCategoriaScreen
import com.example.proyecto_droid.ui.AppViewModelProvider
import com.example.proyecto_droid.ui.screens.lugares.LocalLugarScreen
import com.example.proyecto_droid.ui.screens.categorias.CategoriaScreen
import com.example.proyecto_droid.ui.screens.platos.LocalPlatoScreen
import com.example.proyecto_droid.ui.theme.Proyecto_droidTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de inyección de dependencias
        AppViewModelProvider.appContainer = (application as Proyecto_droid).container

        setContent {
            Proyecto_droidTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}
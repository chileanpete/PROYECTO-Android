package com.example.proyecto_droid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_droid.Proyecto_droid
import com.example.proyecto_droid.ui.theme.MainScreen
import com.example.proyecto_droid.ui.screens.platos.LocalAddPlatoScreen
import com.example.proyecto_droid.ui.screens.lugares.LocalAddLugarScreen
import com.example.proyecto_droid.ui.screens.categorias.AddCategoriaScreen
import com.example.proyecto_droid.ui.AppViewModelProvider
import com.example.proyecto_droid.ui.screens.lugares.LocalLugarScreen
import com.example.proyecto_droid.ui.screens.categorias.CategoriaScreen
import com.example.proyecto_droid.ui.screens.platos.LocalPlatoScreen




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asignar appContainer para el ViewModelProvider
        AppViewModelProvider.appContainer = (application as Proyecto_droid).container

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { MainScreen(navController) }
                        composable("categoria/{categoriaId}") { backStackEntry ->
                            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
                            CategoriaScreen(navController)
                        }
                        composable("contenido/{categoriaId}") { backStackEntry ->
                            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
                            ContenidoScreen(categoriaId, navController)
                        }
                        composable("agregarCategoria") { AgregarCategoriaScreen(navController) }

                        composable("agregar_contenido/{categoriaId}") { backStackEntry ->
                            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
                            AgregarContenidoScreen(navController, categoriaId)
                        }

                        // Por ahora estas dos rutas no implementadas, las agregarás luego:
                        // composable("agregarContenido/{categoriaId}") { ... }
                        // composable("detalleContenido/{contenidoId}") { ... }
                    }

                }
            }
        }
    }
}

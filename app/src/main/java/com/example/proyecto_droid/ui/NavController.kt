package com.example.proyecto_droid.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.proyecto_droid.ui.screens.auth.LoginScreen
import com.example.proyecto_droid.ui.screens.categorias.AddCategoriaScreen
import com.example.proyecto_droid.ui.screens.categorias.CategoriaScreen
import com.example.proyecto_droid.ui.screens.platos.LocalAddPlatoScreen
import com.example.proyecto_droid.ui.screens.platos.LocalPlatoScreen
import com.example.proyecto_droid.ui.screens.lugares.LocalAddLugarScreen
import com.example.proyecto_droid.ui.screens.lugares.LocalLugarScreen
import com.example.proyecto_droid.ui.theme.MainScreen
import kotlinx.serialization.Serializable

//@Serializable
//object Login
//
//@Serializable
//object Platos
//
//@Serializable
//object AddPlato
//
//@Serializable
//object Lugares
//
//@Serializable
//object AddLugar
//
//@Serializable
//object AuthManager

object Routes {
    // Autenticación
//    const val AUTH_MANAGER = "auth_manager"
//    const val LOGIN = "login"

    // Pantalla principal
    const val MAIN = "main"

    // Gestión de Platos
    const val PLATOS = "platos"
    const val ADD_PLATO = "add_plato"

    // Gestión de Lugares
    const val LUGARES = "lugares"
    const val ADD_LUGAR = "add_lugar"

    // Gestión de Categorías
    const val CATEGORIAS = "categorias"
    const val ADD_CATEGORIA = "add_categoria"
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MAIN  // Establece MAIN como pantalla inicial
    ) {
        // Pantalla Principal
        composable(Routes.MAIN) {
            MainScreen(navController = navController)
        }

        // Pantallas de Platos
        composable(Routes.PLATOS) {
            LocalPlatoScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composable(Routes.ADD_PLATO) {
            LocalAddPlatoScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        // Pantallas de Lugares
        composable(Routes.LUGARES) {
            LocalLugarScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composable(Routes.ADD_LUGAR) {
            LocalAddLugarScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        // Pantallas de Categorías
        composable(Routes.CATEGORIAS) {
            CategoriaScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composable(Routes.ADD_CATEGORIA) {
            AddCategoriaScreen(
                navController = navController,
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }
    }
}
//    NavHost(navController = navController,startDestination = AuthManager) {
//        composable<AuthManager> {
//            AuthManager(navController = navController)
//        }
//        composable<Login>{
//            LoginScreen(navController = navController)
//        }
//        composable<Platos> {
//            PlatosScreen(navController = navController)
//        }
//        composable<AddPlato> {
//            AddPlatoScreen(navController = navController)
//        }
//        composable<Lugares> {
//            PlatosScreen(navController = navController)
//        }
//        composable<AddLugar> {
//            AddPlatoScreen(navController = navController)
//        }
//    }

package com.example.proyecto_droid.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_droid.ui.screens.auth.LoginScreen
import com.example.proyecto_droid.ui.screens.platos.AddPlatoScreen
import com.example.proyecto_droid.ui.screens.platos.PlatosScreen
import com.example.proyecto_droid.ui.screens.lugares.AddLugarScreen
import com.example.proyecto_droid.ui.screens.lugares.LugaresScreen
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
    const val AUTH_MANAGER = "auth_manager"
    const val LOGIN = "login"
    const val PLATOS = "platos"
    const val ADD_PLATO = "add_plato"
    const val LUGARES = "lugares"
    const val ADD_LUGAR = "add_lugar"
}

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_MANAGER
    ) {
        // Pantallas de Autenticación
        composable(Routes.AUTH_MANAGER) {
            AuthManager(navController = navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        // Pantallas de Platos
        composable(Routes.PLATOS) {
            PlatosScreen(navController = navController)
        }

        composable(Routes.ADD_PLATO) {
            AddPlatoScreen(navController = navController)
        }

        // Pantallas de Lugares
        composable(Routes.LUGARES) {
            LugaresScreen(navController = navController)
        }

        composable(Routes.ADD_LUGAR) {
            AddLugarScreen(navController = navController)
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
}
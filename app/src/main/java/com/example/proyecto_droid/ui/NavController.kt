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

@Serializable
object Login

@Serializable
object Platos

@Serializable
object AddPlato

@Serializable
object Lugares

@Serializable
object AddLugar

@Serializable
object AuthManager

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController,startDestination = AuthManager) {
        composable<AuthManager> {
            AuthManager(navController = navController)
        }
        composable<Login>{
            LoginScreen(navController = navController)
        }
        composable<Platos> {
            PlatosScreen(navController = navController)
        }
        composable<AddPlato> {
            AddPlatoScreen(navController = navController)
        }
        composable<Lugares> {
            PlatosScreen(navController = navController)
        }
        composable<AddLugar> {
            AddPlatoScreen(navController = navController)
        }
    }
}
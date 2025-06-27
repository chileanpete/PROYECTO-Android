package com.example.proyecto_droid.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_droid.ui.screens.auth.LoginScreen
import com.example.proyecto_droid.ui.screens.platos.AddPlatoScreen
import com.example.proyecto_droid.ui.screens.platos.PlatosScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Platos

@Serializable
object AddPlato

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
    }
}
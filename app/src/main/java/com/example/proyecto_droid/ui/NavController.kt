package com.example.proyecto_droid.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_droid.ui.screens.auth.LoginScreen
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
    const val AUTH_MANAGER = "auth_manager"
    const val LOGIN = "login"

    // Flujo principal
    const val MAIN = "main"
    const val CATEGORIA = "categoria/{categoriaId}"
    const val CONTENIDO = "contenido/{categoriaId}"
    const val AGREGAR_CATEGORIA = "agregar_categoria"
    const val AGREGAR_CONTENIDO = "agregar_contenido/{categoriaId}"

    // Platos
    const val PLATOS = "platos"
    const val ADD_PLATO = "add_plato"

    // Lugares
    const val LUGARES = "lugares"
    const val ADD_LUGAR = "add_lugar"
}

fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
    composable(Routes.MAIN) { MainScreen(navController) }
    composable(Routes.CATEGORIA) { backStackEntry ->
        val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
        CategoriaScreen(categoriaId, navController)
    }
//    composable(Routes.CONTENIDO) { backStackEntry ->
//        val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
//        ContenidoScreen(categoriaId, navController)
//    }
    composable(Routes.AGREGAR_CATEGORIA) {
        AddCategoriaScreen(navController)
    }
//    composable(Routes.AGREGAR_CONTENIDO) { backStackEntry ->
//        val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
//        AddContenidoScreen(navController, categoriaId)
//    }
}

fun NavGraphBuilder.authNavigation(navController: NavHostController) {
    composable(Routes.AUTH_MANAGER) { AuthManager(navController) }
    composable(Routes.LOGIN) { LoginScreen(navController) }
}

fun NavGraphBuilder.platosNavigation(navController: NavHostController) {
    composable(Routes.PLATOS) { LocalPlatoScreen(navController) }
    composable(Routes.ADD_PLATO) { LocalAddPlatoScreen(navController) }
}

fun NavGraphBuilder.lugaresNavigation(navController: NavHostController) {
    composable(Routes.LUGARES) { LocalLugarScreen(navController) }
    composable(Routes.ADD_LUGAR) { LocalAddLugarScreen(navController) }
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
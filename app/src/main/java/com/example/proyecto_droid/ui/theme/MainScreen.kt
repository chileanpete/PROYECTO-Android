package com.example.proyecto_droid.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyecto_droid.ui.Routes

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(Routes.PLATOS) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestión de Platos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.LUGARES) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestión de Lugares")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.CATEGORIAS) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestión de Categorías")
        }
    }
}

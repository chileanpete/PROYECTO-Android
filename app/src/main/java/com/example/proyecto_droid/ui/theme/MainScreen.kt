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

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("categoria/1") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Películas")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("categoria/2") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Series")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("categoria/3") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Anime")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("agregarcategoria") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Categoría")
        }
    }
}

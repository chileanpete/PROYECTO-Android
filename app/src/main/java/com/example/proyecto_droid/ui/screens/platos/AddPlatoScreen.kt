package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.ui.screens.auth.AuthUiState

@Composable
fun AddPlatoScreen(
    viewModel: PlatosViewModel = viewModel(
        factory =  com.example.proyecto_droid.ui.AppViewModelProvider.Factory

    ),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.resetUiState()
    }

    var idLugarText by remember { mutableStateOf("") }
    var idCategoriaText by remember { mutableStateOf("") }
    var nombrePlato by remember { mutableStateOf("") }
    var descripcionPlato by remember { mutableStateOf("") }
    var precioPlatoText by remember { mutableStateOf("") }
    var caloriasPlatoText by remember { mutableStateOf("") }
    var protePlatoText by remember { mutableStateOf("") }
    var carbPlatoText by remember { mutableStateOf("") }
    var grasaPlatoText by remember { mutableStateOf("") }
    var fibraPlatoText by remember { mutableStateOf("") }
    var azucarPlatoText by remember { mutableStateOf("") }
    var sodioPlatoText by remember { mutableStateOf("") }
    var disponibilidadPlato by remember { mutableStateOf(true) }
    var esVegetariano by remember { mutableStateOf(false) }
    var esVegano by remember { mutableStateOf(false) }
    var sinGluten by remember { mutableStateOf(false) }
    var imagenUrl by remember { mutableStateOf("") }
    //var fechaCreacion by remember {java.time.LocalDateTime.now().toString() }




    val platosState = viewModel.platosUiState
    var shouldRedirect by remember { mutableStateOf(true) }

    // Redirección si se agregó la tarea con éxito
    LaunchedEffect(platosState) {
        if (shouldRedirect && platosState is PlatosUiState.Success) {
            shouldRedirect = false
            navController.popBackStack()
        }
    }
    val idLugar = idLugarText.toIntOrNull() ?:0
    val idCategoria = idCategoriaText.toIntOrNull() ?:0
    val precioPlato =precioPlatoText.toDoubleOrNull() ?:0.0
    val caloriasPlato = caloriasPlatoText.toIntOrNull() ?:0
    val protePlato =protePlatoText.toDoubleOrNull() ?:0.0
    val carbPlato =carbPlatoText.toDoubleOrNull() ?:0.0
    val grasaPlato =grasaPlatoText.toDoubleOrNull() ?:0.0
    val fibraPlato =fibraPlatoText.toDoubleOrNull() ?:0.0
    val azucarPlato =azucarPlatoText.toDoubleOrNull() ?:0.0
    val sodioPlato =sodioPlatoText.toDoubleOrNull() ?:0.0

    //
    // Despues anado el resto de campos de textso de abajo
    //
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Añadir Plato", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = idLugarText,
            onValueChange = { idLugarText = it },
            label = { Text("Id Lugar") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = idCategoriaText,
            onValueChange = { idCategoriaText = it },
            label = { Text("Id Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.addPlato(idLugar,idCategoria,nombrePlato,descripcionPlato,precioPlato, caloriasPlato,protePlato,
                carbPlato,grasaPlato,fibraPlato,azucarPlato, sodioPlato, disponibilidadPlato,esVegetariano, esVegano,
                sinGluten, imagenUrl) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Plato")
        }

        Spacer(modifier = Modifier.height(24.dp))

    }
}
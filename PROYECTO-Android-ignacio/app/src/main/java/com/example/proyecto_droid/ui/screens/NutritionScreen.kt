package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.viewmodel.FavoritoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NutritionScreen(viewModel: FavoritoViewModel = viewModel()) {
    var idUsuario by remember { mutableStateOf("") }
    var idPlato by remember { mutableStateOf("") }
    val fechaActual = remember { getCurrentDate() }
    val favoritos by viewModel.favoritos.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Agregar Plato Favorito", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = idUsuario,
                onValueChange = { idUsuario = it },
                label = { Text("ID Usuario") },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = idPlato,
                onValueChange = { idPlato = it },
                label = { Text("ID Plato") },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (idUsuario.isNotBlank() && idPlato.isNotBlank()) {
                        val favorito = PlatoFavoritoEntity(
                            id_usuario = idUsuario.toIntOrNull() ?: 0,
                            id_plato = idPlato.toIntOrNull() ?: 0,
                            fecha_agregado = fechaActual
                        )
                        viewModel.add(favorito)
                        idUsuario = ""
                        idPlato = ""
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar en Menu Favorito", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Menu Favorito", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(items = favoritos, key = { it.id_favorito }) { favorito ->
            FavoritoCard(favorito = favorito, onDelete = { viewModel.remove(favorito) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun FavoritoCard(favorito: PlatoFavoritoEntity, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Usuario: ${favorito.id_usuario}", fontWeight = FontWeight.Bold)
            Text("Plato: ${favorito.id_plato}")
            Text("Fecha: ${favorito.fecha_agregado}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDelete,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Eliminar", color = Color.White)
            }
        }
    }
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}
package com.example.proyecto_droid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.proyecto_droid.ui.viewmodel.TallerViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_droid.data.local.entity.TallerEntity
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText

@Composable
fun AddTallerScreen(fechaSeleccionada: String, viewModel: TallerViewModel, navController: NavHostController){
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var duracion_minutos by remember { mutableStateOf("") }
    var nivel_dificultad by remember { mutableStateOf("") }
    var cupo_maximo by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fecha_inicio by remember { mutableStateOf("") }
    var fecha_fin by remember { mutableStateOf("") }
    var imagen_url by remember { mutableStateOf("") }
    var requisitos by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Nuevo taller para $fechaSeleccionada")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = instructor,
            onValueChange = { instructor = it },
            label = { Text("Instructor") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = duracion_minutos,
            onValueChange = { duracion_minutos = it },
            label = { Text("Duracion(min)") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = nivel_dificultad,
            onValueChange = { nivel_dificultad = it },
            label = { Text("Nivel de Dificultad") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = cupo_maximo,
            onValueChange = { cupo_maximo = it },
            label = { Text("Cupo Maximo") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = costo,
            onValueChange = { costo = it },
            label = { Text("Costo") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicacion") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = fecha_inicio,
            onValueChange = { fecha_inicio = it },
            label = { Text("Fecha Inico") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = fecha_fin,
            onValueChange = { fecha_fin = it },
            label = { Text("Fecha Final") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = imagen_url,
            onValueChange = { imagen_url = it },
            label = { Text("Ingrese imagen (opcional)") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = requisitos,
            onValueChange = { requisitos = it },
            label = { Text("Requisitos") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText))

        Button(
            onClick = {
                val taller = TallerEntity(
                    id_taller = 0,
                    nombre = nombre,
                    descripcion = descripcion,
                    instructor = instructor,
                    categoria = categoria,
                    duracion_minutos = duracion_minutos.toIntOrNull() ?: 0,
                    nivel_dificultad = nivel_dificultad,
                    cupo_maximo = cupo_maximo.toIntOrNull() ?: 0,
                    costo = costo.toDoubleOrNull() ?: 0.0,
                    ubicacion = ubicacion,
                    fecha_inicio = fechaSeleccionada,
                    fecha_fin = fechaSeleccionada,
                    activo = true,
                    imagen_url = imagen_url,
                    requisitos = requisitos
                )

                viewModel.addTaller(taller)
                Toast.makeText(context, "Taller agregado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ){
            Text("Guardar Taller")
        }
    }
}
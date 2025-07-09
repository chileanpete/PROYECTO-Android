package com.example.proyecto_droid.ui.viewmodel

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddFavoritoForm(idUsuario: Int, viewModel: FavoritoViewModel) {
    var idPlato by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaActual = sdf.format(Date())

    Column {
        OutlinedTextField(
            value = idPlato,
            onValueChange = { idPlato = it },
            label = { Text("ID del Plato") },
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                unfocusedBorderColor = LightGrayText)
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val nuevo = PlatoFavoritoEntity(
                    id_usuario = idUsuario,
                    id_plato = idPlato.toIntOrNull() ?: 0,
                    fecha_agregado = fechaActual
                )
                viewModel.add(nuevo)
                Toast.makeText(context, "Plato favorito agregado", Toast.LENGTH_SHORT).show()
                idPlato = ""
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Guardar Plato", color = Color.White)
        }
    }
}
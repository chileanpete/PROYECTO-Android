package com.example.proyecto_droid.ui.screens

import android.icu.util.Calendar
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.R
import com.example.proyecto_droid.data.local.entity.TallerEntity
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.viewmodel.FavoritoViewModel
import com.example.proyecto_droid.ui.viewmodel.SessionViewModel
import com.example.proyecto_droid.ui.viewmodel.TallerViewModel

// Datos mock para tips y eventos

data class Tip(val imageRes: Int, val title: String, val description: String, val tag: String)
data class Event(val imageRes: Int, val title: String, val description: String, val tag: String)

val foodTips = listOf(
    Tip(R.drawable.ic_nutrition, "Come más verduras", "Incluye al menos 5 porciones de verduras al día.", "Comida"),
    Tip(R.drawable.ic_nutrition, "Bebe agua", "Mantente hidratado durante el día.", "Comida"),
    Tip(R.drawable.ic_nutrition, "Evita azúcares", "Reduce el consumo de bebidas azucaradas.", "Comida")
)

val exerciseTips = listOf(
    Tip(R.drawable.ic_exercise, "Camina 30 min", "Realiza caminatas diarias para mejorar tu salud.", "Ejercicio"),
    Tip(R.drawable.ic_exercise, "Estiramientos", "Haz estiramientos antes y después de ejercitarte.", "Ejercicio"),
    Tip(R.drawable.ic_exercise, "Descansa bien", "El descanso es clave para la recuperación muscular.", "Ejercicio")
)

val events = listOf(
    Event(R.drawable.ic_events, "Maratón Universitaria", "Participa en la maratón anual de la universidad.", "Evento"),
    Event(R.drawable.ic_events, "Feria Saludable", "Descubre productos y charlas sobre vida sana.", "Evento")
)

@Composable
fun HomeScreen(navController: NavController, sessionViewModel: SessionViewModel,
               tallerViewModel: TallerViewModel = viewModel(), favoritoViewModel: FavoritoViewModel = viewModel()) {
    val sessionState by sessionViewModel.uiState.collectAsState()
    val userName = sessionState.currentUser?.nombre?.takeIf { it.isNotBlank() } ?: "¡Bienvenido!"

    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember {
        mutableStateOf("${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}")
    }

    val talleres by tallerViewModel.getByFecha(selectedDate).collectAsState(initial = emptyList())
    val favoritos by favoritoViewModel.favoritos.collectAsState()

    val tips_comida = stringResource(R.string.tips_comida)
    val tips_ejercicio = stringResource(R.string.tips_ejercicio)
    val eventos = stringResource(R.string.eventos)
    val calendario = stringResource(R.string.calendario)
    val agregar_taller = stringResource(R.string.agregar_taller)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundLight)
    ) {
        // Gradiente superior con saludo y búsqueda
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GreenPrimary, Color.White),
                        startY = 0f,
                        endY = 400f
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp, end = 20.dp)
            ) {
                Text(
                    text = "Hola, $userName!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Buscar...", color = LightGrayText) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = tips_comida)
        LazyRow(contentPadding = PaddingValues(horizontal = 12.dp)) {
            items(foodTips) { tip ->
                ModernTipCard(tip, circleColor = GreenPrimary)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = tips_ejercicio)
        LazyRow(contentPadding = PaddingValues(horizontal = 12.dp)) {
            items(exerciseTips) { tip ->
                ModernTipCard(tip, circleColor = Color(0xFF1976D2))
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = eventos)
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            events.forEach { event ->
                ModernEventCard(event, circleColor = Color(0xFFFBC02D))
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = calendario)

        AndroidView(factory = { context ->
            CalendarView(context).apply {
                setOnDateChangeListener { _, year, month, day ->
                    selectedDate = "$year-${month + 1}-$day"
                }
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp))

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate("add_taller/${selectedDate}")
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = agregar_taller, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
        SectionTitle("Talleres para el $selectedDate")
        talleres.forEach { TallerCard(it) }

    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ModernTipCard(tip: Tip, circleColor: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(220.dp)
            .height(110.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .size(48.dp)
                    .background(circleColor.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = tip.imageRes),
                    contentDescription = tip.title,
                    tint = circleColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(tip.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(tip.description, fontSize = 12.sp, maxLines = 2, color = Color.DarkGray)
                Surface(
                    color = circleColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = tip.tag,
                        color = circleColor,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ModernEventCard(event: Event, circleColor: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .size(48.dp)
                    .background(circleColor.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = event.imageRes),
                    contentDescription = event.title,
                    tint = circleColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(event.description, fontSize = 12.sp, maxLines = 2, color = Color.DarkGray)
                Surface(
                    color = circleColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = event.tag,
                        color = circleColor,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TallerCard(taller: TallerEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Nombre: ${taller.nombre}", fontWeight = FontWeight.Bold)
            Text("Instructor: ${taller.instructor}")
            //Text("Descripción: ${taller.descripcion}")
            Text("Categoría: ${taller.categoria}")
            Text("Duración: ${taller.duracion_minutos} min")
            Text("Dificultad: ${taller.nivel_dificultad}")
            //Text("Cupo: ${taller.cupo_maximo}")
            //Text("Costo: \$${taller.costo}")
            Text("Ubicación: ${taller.ubicacion}")
            Text("Fecha: ${taller.fecha_inicio}")
        }
    }
}

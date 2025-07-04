package com.example.proyecto_droid.ui.screens.desafios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.proyecto_droid.util.Constants


// --- Retrofit API ---

interface ApiService {
    @GET("desafios")  // Asegúrate que esta ruta sea correcta en tu backend
    suspend fun getDesafios(): List<DesafioApiModel>
}

// Modelo para recibir JSON (usa los nombres tal cual están en la API)
data class DesafioApiModel(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo_desafio: String,
    val objetivos_relacionados: List<String> // Debe ser un array JSON en la API
)

// Data class usada en UI con enum
data class Desafio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: TipoDesafio,
    val objetivosRelacionados: List<String>
)

enum class TipoDesafio {
    DIARIO, SEMANAL
}

val retrofit = Retrofit.Builder()
    .baseUrl("${Constants.BASE_URL}/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)


// --- Composable ---

@Composable
fun DesafiosScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    val objetivosDisponibles = listOf(
        "Mejorar salud cardiovascular",
        "Perder peso",
        "Aumentar fuerza muscular",
        "Mejorar flexibilidad",
        "Reducir estrés"
    )

    var mostrarDialogo by remember { mutableStateOf(true) }
    val seleccionados = remember { mutableStateListOf<String>() }

    var desafios by remember { mutableStateOf<List<Desafio>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    fun cargarDesafios() {
        isLoading = true
        errorMsg = null
        scope.launch {
            try {
                val response = apiService.getDesafios()
                desafios = response.map {
                    Desafio(
                        id = it.id,
                        titulo = it.titulo,
                        descripcion = it.descripcion,
                        tipo = when (it.tipo_desafio.lowercase()) {
                            "diario" -> TipoDesafio.DIARIO
                            "semanal" -> TipoDesafio.SEMANAL
                            else -> TipoDesafio.DIARIO
                        },
                        objetivosRelacionados = it.objetivos_relacionados
                    )
                }
            } catch (e: Exception) {
                errorMsg = "Error cargando desafíos: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(mostrarDialogo) {
        if (!mostrarDialogo) {
            cargarDesafios()
        }
    }

    val desafiosFiltrados = desafios.filter { desafio ->
        desafio.objetivosRelacionados.any { it in seleccionados }
    }

    val desafiosDiarios = desafiosFiltrados.filter { it.tipo == TipoDesafio.DIARIO }
    val desafiosSemanales = desafiosFiltrados.filter { it.tipo == TipoDesafio.SEMANAL }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 6.dp,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿Cuál es tu objetivo?",
                        style = MaterialTheme.typography.titleLarge,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selecciona uno o más para sugerirte desafíos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    objetivosDisponibles.forEach { objetivo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (objetivo in seleccionados) {
                                        seleccionados.remove(objetivo)
                                    } else {
                                        seleccionados.add(objetivo)
                                    }
                                }
                        ) {
                            Checkbox(
                                checked = objetivo in seleccionados,
                                onCheckedChange = {
                                    if (it) seleccionados.add(objetivo)
                                    else seleccionados.remove(objetivo)
                                }
                            )
                            Text(objetivo)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { if (seleccionados.isNotEmpty()) mostrarDialogo = false },
                    enabled = seleccionados.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    if (!mostrarDialogo) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Desafíos sugeridos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMsg != null) {
                Text(
                    text = errorMsg ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    // Mostrar encabezado y lista de desafíos diarios
                    if (desafiosDiarios.isNotEmpty()) {
                        item {
                            Text(
                                text = "Desafíos Diarios",
                                style = MaterialTheme.typography.titleLarge.copy(color = GreenPrimary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(desafiosDiarios) { desafio ->
                            DesafioCard(desafio)
                        }
                    }

                    // Mostrar encabezado y lista de desafíos semanales
                    if (desafiosSemanales.isNotEmpty()) {
                        item {
                            Text(
                                text = "Desafíos Semanales",
                                style = MaterialTheme.typography.titleLarge.copy(color = GreenPrimary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(desafiosSemanales) { desafio ->
                            DesafioCard(desafio)
                        }
                    }

                    if (desafiosDiarios.isEmpty() && desafiosSemanales.isEmpty()) {
                        item {
                            Text(
                                text = "No hay desafíos para los objetivos seleccionados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}

// Card para mostrar un desafío (lo separé para limpiar el código)
@Composable
fun DesafioCard(desafio: Desafio) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(desafio.titulo, style = MaterialTheme.typography.titleMedium)
            Text(desafio.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Acción al "Probar" desafío
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Probar", color = Color.White)
            }
        }
    }
}


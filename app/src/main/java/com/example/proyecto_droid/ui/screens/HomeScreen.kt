package com.example.proyecto_droid.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_droid.data.model.TallerRecreativo
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.viewmodel.SessionViewModel
import com.example.proyecto_droid.ui.viewmodel.TallerViewModel
import com.example.proyecto_droid.ui.viewmodel.TallerEvent
import com.example.proyecto_droid.ui.screens.components.CustomCalendar
import com.example.proyecto_droid.ui.screens.components.toDateString
import com.example.proyecto_droid.ui.screens.components.toLocalDateOrNull
import java.time.LocalDate
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, 
    sessionViewModel: SessionViewModel,
    tallerViewModel: TallerViewModel = viewModel()
) {
    val sessionState by sessionViewModel.uiState.collectAsState()
    val tallerUiState by tallerViewModel.uiState.collectAsState()
    val userName = sessionState.currentUser?.nombre?.takeIf { it.isNotBlank() } ?: "Usuario"

    // Fecha inicial - hoy
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedDateString = selectedDate.toDateString()

    // Obtener todos los talleres
    val allTalleres = tallerUiState.talleres
    
    // Debug: Log de talleres cargados
    LaunchedEffect(allTalleres) {
        Log.d("HomeScreen", "Talleres cargados: ${allTalleres.size}")
        allTalleres.forEach { taller ->
            Log.d("HomeScreen", "Taller: ${taller.nombre}, Fecha: ${taller.fechaInicio}")
        }
        
        // Log directo de fechas procesadas
        val fechasDirectas = allTalleres.map { it.fechaInicio.substring(0, 10) }.distinct().sorted()
        Log.d("HomeScreen", "Fechas procesadas directamente: $fechasDirectas")
        Log.d("HomeScreen", "Fecha seleccionada actual: $selectedDateString")
    }
    
    // Obtener fechas únicas que tienen talleres - usando remember directo
    val fechasConTalleres by remember(allTalleres) {
        mutableStateOf(
            allTalleres.map { 
                it.fechaInicio.substring(0, 10) // Extraer solo YYYY-MM-DD
            }.distinct().sorted()
        )
    }
    
    // Obtener talleres de la fecha seleccionada - usando remember directo
    val talleresSelectedDate by remember(allTalleres, selectedDateString) {
        mutableStateOf(
            allTalleres.filter { taller ->
                taller.fechaInicio.startsWith(selectedDateString)
            }
        )
    }
    
    // Debug logs
    LaunchedEffect(fechasConTalleres) {
        Log.d("HomeScreen", "fechasConTalleres actualizado: $fechasConTalleres")
    }
    
    LaunchedEffect(talleresSelectedDate) {
        Log.d("HomeScreen", "talleresSelectedDate actualizado para $selectedDateString: ${talleresSelectedDate.size}")
    }
    
    // Log adicional para debug fuera del remember
    Log.d("HomeScreen", "Variables actuales - selectedDateString: $selectedDateString, fechasConTalleres.size: ${fechasConTalleres.size}")

    // Refrescar talleres cuando se monta el composable
    LaunchedEffect(Unit) {
        tallerViewModel.handleEvent(TallerEvent.RefreshTalleres)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header con gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GreenPrimary, GreenPrimary.copy(alpha = 0.8f))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "¡Bienvenido de vuelta!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Light
                            )
                        )
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Gestiona tus talleres y actividades",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                    
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Dashboard",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                        )
                    }
                }
            }
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            // Indicador de carga
            if (tallerUiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            
            // Mensaje de error
            if (tallerUiState.hasError) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar talleres",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tallerUiState.errorMessage ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { tallerViewModel.handleEvent(TallerEvent.RefreshTalleres) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary
                            )
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            // Sección de calendario
            if (!tallerUiState.isLoading && !tallerUiState.hasError) {
                Log.d("HomeScreen", "Mostrando calendario con ${fechasConTalleres.size} fechas: $fechasConTalleres")
                
                Text(
                    text = "Calendario",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CustomCalendar(
                    selectedDate = selectedDate,
                    fechasConTalleres = fechasConTalleres,
                    onDateSelected = { newDate ->
                        Log.d("HomeScreen", "Fecha seleccionada cambiada: ${newDate.toDateString()}")
                        selectedDate = newDate
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Talleres para la fecha seleccionada
                Log.d("HomeScreen", "Verificando talleres para fecha $selectedDateString")
                Log.d("HomeScreen", "talleresSelectedDate.size: ${talleresSelectedDate.size}")
                Log.d("HomeScreen", "talleresSelectedDate: $talleresSelectedDate")
                
                if (talleresSelectedDate.isNotEmpty()) {
                    Log.d("HomeScreen", "Mostrando ${talleresSelectedDate.size} talleres para $selectedDateString")
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Talleres programados",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    talleresSelectedDate.forEach { taller ->
                        Log.d("HomeScreen", "Mostrando card para: ${taller.nombre}")
                        ModernTallerCard(taller = taller)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                    Log.d("HomeScreen", "No hay talleres para $selectedDateString - mostrando mensaje vacío")
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No hay talleres programados",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "para el $selectedDateString",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // Espaciado adicional al final
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun FechaChip(
    fecha: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isSelected) 4.dp else 2.dp,
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = fecha,
                fontSize = 13.sp,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = CircleShape,
                color = if (isSelected) Color.White.copy(alpha = 0.2f) else GreenPrimary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "$count",
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ModernTallerCard(taller: TallerRecreativo) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = taller.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GreenPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = taller.categoria,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GreenPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TallerInfoItem(
                    label = "Instructor",
                    value = taller.instructor,
                    modifier = Modifier.weight(1f)
                )
                TallerInfoItem(
                    label = "Duración",
                    value = "${taller.duracionMinutos}m",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TallerInfoItem(
                    label = "Ubicación",
                    value = taller.ubicacion,
                    modifier = Modifier.weight(1f)
                )
                TallerInfoItem(
                    label = "Costo",
                    value = if (taller.costo > 0) "$${taller.costo}" else "Gratis",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TallerInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
} 
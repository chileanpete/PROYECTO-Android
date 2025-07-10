package com.example.proyecto_droid.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_droid.ui.theme.GreenPrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    fechasConTalleres: List<String>, // Fechas en formato "YYYY-MM-DD"
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    // Convertir fechas con talleres a LocalDate para comparación
    val fechasConTalleresSet = remember(fechasConTalleres) {
        fechasConTalleres.mapNotNull { fecha ->
            try {
                LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                null
            }
        }.toSet()
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header del calendario con navegación
            CalendarHeader(
                currentMonth = currentMonth,
                onPreviousMonth = {
                    currentMonth = currentMonth.minusMonths(1)
                },
                onNextMonth = {
                    currentMonth = currentMonth.plusMonths(1)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Días de la semana
            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(8.dp))

            // Calendario principal
            CalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                fechasConTalleres = fechasConTalleresSet,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Mes anterior",
                tint = GreenPrimary
            )
        }

        Text(
            text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Mes siguiente",
                tint = GreenPrimary
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader() {
    val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")
    
    // Reemplazar LazyVerticalGrid con Row simple
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    fechasConTalleres: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    
    // Calcular el primer día de la semana (Lunes = 1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    
    // Crear lista de días para mostrar
    val daysToShow = mutableListOf<LocalDate?>()
    
    // Agregar días vacíos al inicio
    repeat(firstDayOfWeek) {
        daysToShow.add(null)
    }
    
    // Agregar todos los días del mes
    var currentDay = firstDayOfMonth
    while (currentDay <= lastDayOfMonth) {
        daysToShow.add(currentDay)
        currentDay = currentDay.plusDays(1)
    }
    
    // Completar la última fila si es necesario
    while (daysToShow.size % 7 != 0) {
        daysToShow.add(null)
    }

    // Reemplazar LazyVerticalGrid con Column de filas
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Dividir los días en filas de 7
        daysToShow.chunked(7).forEach { weekDays ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                weekDays.forEach { day ->
                    if (day != null) {
                        CalendarDay(
                            date = day,
                            isSelected = day == selectedDate,
                            hasTaller = day in fechasConTalleres,
                            isToday = day == LocalDate.now(),
                            onClick = { onDateSelected(day) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        // Espacio vacío
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    hasTaller: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(
                color = when {
                    isSelected -> GreenPrimary
                    isToday -> GreenPrimary.copy(alpha = 0.2f)
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (hasTaller && !isSelected) 2.dp else 0.dp,
                color = GreenPrimary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isSelected -> Color.White
                        isToday -> GreenPrimary
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                ),
                fontSize = 14.sp
            )
            
            // Indicador de taller (punto pequeño)
            if (hasTaller && !isSelected) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = GreenPrimary,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

// Función de extensión para facilitar el uso
fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (e: Exception) {
        null
    }
}

fun LocalDate.toDateString(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
} 
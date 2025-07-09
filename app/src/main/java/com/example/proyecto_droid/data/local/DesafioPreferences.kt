package com.example.proyecto_droid.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

val Context.desafioDataStore by preferencesDataStore(name = "desafio_prefs")

object DesafioPreferences {
    private fun weekKey(userId: Int): Preferences.Key<String> = stringPreferencesKey("objetivos_semana_usuario_${userId}")
    private fun weekDateKey(userId: Int): Preferences.Key<String> = stringPreferencesKey("fecha_semana_usuario_${userId}")
    private fun dayKey(userId: Int): Preferences.Key<String> = stringPreferencesKey("objetivos_dia_usuario_${userId}")
    private fun dayDateKey(userId: Int): Preferences.Key<String> = stringPreferencesKey("fecha_dia_usuario_${userId}")

    suspend fun saveWeeklyObjectives(context: Context, userId: Int, objetivos: List<String>, fechaSemana: String) {
        context.desafioDataStore.edit { prefs ->
            prefs[weekKey(userId)] = objetivos.joinToString("|||")
            prefs[weekDateKey(userId)] = fechaSemana
        }
    }

    suspend fun saveDailyObjectives(context: Context, userId: Int, objetivos: List<String>, fechaDia: String) {
        context.desafioDataStore.edit { prefs ->
            prefs[dayKey(userId)] = objetivos.joinToString("|||")
            prefs[dayDateKey(userId)] = fechaDia
        }
    }

    fun getWeeklyObjectives(context: Context, userId: Int): Flow<Pair<List<String>?, String?>> =
        context.desafioDataStore.data.map { prefs ->
            val objetivos = prefs[weekKey(userId)]?.split("|||")?.filter { it.isNotBlank() }
            val fecha = prefs[weekDateKey(userId)]
            Pair(objetivos, fecha)
        }

    fun getDailyObjectives(context: Context, userId: Int): Flow<Pair<List<String>?, String?>> =
        context.desafioDataStore.data.map { prefs ->
            val objetivos = prefs[dayKey(userId)]?.split("|||")?.filter { it.isNotBlank() }
            val fecha = prefs[dayDateKey(userId)]
            Pair(objetivos, fecha)
        }

    fun getCurrentWeekStart(): String {
        val today = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val startOfWeek = today.with(weekFields.dayOfWeek(), 1)
        return startOfWeek.toString()
    }

    fun getCurrentDay(): String = LocalDate.now().toString()
} 
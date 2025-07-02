package com.example.proyecto_droid.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object TokenManager {
    private const val TOKEN_KEY = "auth_token"
    private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN_KEY)] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[stringPreferencesKey(TOKEN_KEY)]
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(TOKEN_KEY))
        }
    }
}
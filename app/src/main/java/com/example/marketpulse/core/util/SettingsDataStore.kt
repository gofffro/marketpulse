package com.example.marketpulse.core.util

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val BASE_FIAT = stringPreferencesKey("baseFiat")
        val CACHE_TTL_MIN = intPreferencesKey("cacheTtlMinutes")
        val AUTO_REFRESH = booleanPreferencesKey("autoRefresh")
    }

    val baseFiat: Flow<String> = context.dataStore.data.map { it[Keys.BASE_FIAT] ?: "eur" }
    val cacheTtlMinutes: Flow<Int> = context.dataStore.data.map { it[Keys.CACHE_TTL_MIN] ?: 15 }
    val autoRefresh: Flow<Boolean> = context.dataStore.data.map { it[Keys.AUTO_REFRESH] ?: true }

    suspend fun setBaseFiat(v: String) {
        context.dataStore.edit { it[Keys.BASE_FIAT] = v.lowercase() }
    }

    suspend fun setCacheTtlMinutes(v: Int) {
        context.dataStore.edit { it[Keys.CACHE_TTL_MIN] = v.coerceIn(1, 24 * 60) }
    }

    suspend fun setAutoRefresh(v: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_REFRESH] = v }
    }
}

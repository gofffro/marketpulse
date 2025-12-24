package com.example.marketpulse.presentation.screen_settings

data class SettingsUiState(
    val baseFiat: String = "eur",
    val cacheTtlMinutes: Int = 15,
    val autoRefresh: Boolean = true
)

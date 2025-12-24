package com.example.marketpulse.presentation.screen_detail

import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.domain.model.Quote

data class DetailUiState(
    val symbol: String,
    val type: AssetType,
    val isLoading: Boolean = true,
    val quote: Quote? = null,
    val isFavorite: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false
)

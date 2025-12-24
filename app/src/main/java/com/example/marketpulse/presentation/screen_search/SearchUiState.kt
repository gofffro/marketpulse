package com.example.marketpulse.presentation.screen_search

import com.example.marketpulse.domain.model.Asset

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Asset> = emptyList(),
    val error: String? = null
)

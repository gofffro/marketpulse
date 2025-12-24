package com.example.marketpulse.presentation.screen_home

import com.example.marketpulse.domain.model.FiatRate
import com.example.marketpulse.domain.model.Quote

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val favoritesQuotes: List<Quote> = emptyList(),
    val fiatRates: List<FiatRate> = emptyList(),
    val isOffline: Boolean = false,
    val error: String? = null,
    val lastUpdatedMillis: Long? = null
)

sealed interface HomeEvent {
    data object OnRefresh : HomeEvent
    data object OnRetry : HomeEvent
}

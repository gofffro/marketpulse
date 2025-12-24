package com.example.marketpulse.presentation.screen_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketpulse.core.util.SettingsDataStore
import com.example.marketpulse.domain.model.Asset
import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.domain.repository.MarketRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: MarketRepository,
    private val settings: SettingsDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    // Упрощённый список крипты “популярное”
    private val defaultCrypto = listOf(
        Asset("BINANCE:BTCUSDT", "Bitcoin / USDT", AssetType.CRYPTO),
        Asset("BINANCE:ETHUSDT", "Ethereum / USDT", AssetType.CRYPTO)
    )

    init {
        observeData()
        viewModelScope.launch {
            val auto = settings.autoRefresh.first()
            if (auto) refreshAll(force = false)
            else _state.update { it.copy(isLoading = false) }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                repo.observeQuotesForFavorites(),
                settings.baseFiat.flatMapLatest { base ->
                    repo.observeFiatRates(base, targets = listOf("usd", "rub", "jpy"))
                },
                repo.observeIsOffline()
            ) { favQuotes, fiat, offline ->
                Triple(favQuotes, fiat, offline)
            }.collect { (favQuotes, fiat, offline) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        favoritesQuotes = favQuotes,
                        fiatRates = fiat,
                        isOffline = offline
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnRefresh -> refreshAll(force = true)
            HomeEvent.OnRetry -> refreshAll(force = true)
        }
    }

    private fun refreshAll(force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            runCatching { repo.refreshAll(force) }
                .onFailure { _state.update { s -> s.copy(error = it.message ?: "Ошибка", isRefreshing = false) } }
                .onSuccess { _state.update { s -> s.copy(isRefreshing = false) } }
        }
    }

    fun ensureDefaultCryptoInFavorites() {
        viewModelScope.launch {
            // Добавим крипту в избранное при первом запуске, если пусто
            val currentFav = repo.observeFavorites().first()
            if (currentFav.isEmpty()) {
                defaultCrypto.forEach { repo.toggleFavorite(it) }
                repo.refreshFavoritesQuotes(force = true)
            }
        }
    }
}

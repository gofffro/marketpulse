package com.example.marketpulse.presentation.screen_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketpulse.domain.model.Asset
import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.domain.repository.MarketRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: MarketRepository,
    private val symbol: String,
    private val type: AssetType
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState(symbol = symbol, type = type))
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        observe()
        refresh(force = false)
    }

    private fun observe() {
        viewModelScope.launch {
            combine(
                repo.observeFavorites(),
                repo.observeQuotesForFavorites(),
                repo.observeIsOffline()
            ) { favs, quotes, offline ->
                Triple(favs, quotes, offline)
            }.collect { (favs, quotes, offline) ->
                val fav = favs.any { it.symbol == symbol }
                val q = quotes.firstOrNull { it.symbol == symbol && it.type == type }
                _state.update { it.copy(isLoading = false, isFavorite = fav, quote = q, isOffline = offline) }
            }
        }
    }

    fun refresh(force: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                if (!_state.value.isFavorite) {
                    repo.toggleFavorite(Asset(symbol, symbol, type))
                }
                repo.refreshFavoritesQuotes(force = true)
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Ошибка") }
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repo.toggleFavorite(Asset(symbol, symbol, type))
        }
    }
}

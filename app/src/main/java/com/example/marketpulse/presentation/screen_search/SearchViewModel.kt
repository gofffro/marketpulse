package com.example.marketpulse.presentation.screen_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketpulse.domain.model.Asset
import com.example.marketpulse.domain.repository.MarketRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: MarketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        bindSearch()
    }

    fun onQueryChange(text: String) {
        _state.update { it.copy(query = text, error = null) }
        queryFlow.value = text
    }

    fun toggleFavorite(asset: Asset) {
        viewModelScope.launch { repo.toggleFavorite(asset) }
    }

    @OptIn(FlowPreview::class)
    private fun bindSearch() {
        viewModelScope.launch {
            queryFlow
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    if (q.isBlank()) {
                        _state.update { it.copy(results = emptyList(), isLoading = false) }
                        return@collectLatest
                    }
                    _state.update { it.copy(isLoading = true, error = null) }
                    runCatching { repo.searchAssets(q) }
                        .onSuccess { list -> _state.update { it.copy(results = list, isLoading = false) } }
                        .onFailure { e -> _state.update { it.copy(error = e.message ?: "Ошибка", isLoading = false) } }
                }
        }
    }
}

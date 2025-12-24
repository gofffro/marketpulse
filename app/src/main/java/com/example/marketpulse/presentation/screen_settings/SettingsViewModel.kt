package com.example.marketpulse.presentation.screen_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketpulse.core.util.SettingsDataStore
import com.example.marketpulse.domain.repository.MarketRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settings: SettingsDataStore,
    private val repo: MarketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(settings.baseFiat, settings.cacheTtlMinutes, settings.autoRefresh) { b, ttl, auto ->
                SettingsUiState(b, ttl, auto)
            }.collect { _state.value = it }
        }
    }

    fun setBaseFiat(v: String) = viewModelScope.launch { settings.setBaseFiat(v) }
    fun setTtl(v: Int) = viewModelScope.launch { settings.setCacheTtlMinutes(v) }
    fun setAuto(v: Boolean) = viewModelScope.launch { settings.setAutoRefresh(v) }

    fun clearCache() = viewModelScope.launch {
        // простой способ “инвалидации”: refreshAll(force=true) перезапишет, но очистку БД сделаем на стороне DAO
        // (если хочешь прям очистку — добавь методы clear() в репозитории и дерни dao.clear())
        repo.refreshAll(force = true)
    }
}

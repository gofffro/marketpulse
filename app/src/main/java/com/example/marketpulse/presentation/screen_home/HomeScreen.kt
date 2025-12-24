@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketpulse.presentation.screen_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.marketpulse.core.util.AppContainer
import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.presentation.components.*
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun HomeScreen(
    container: AppContainer,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenDetail: (AssetType, String) -> Unit
) {
    val vm = remember {
        HomeViewModel(container.marketRepository, container.settingsDataStore())
    }
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.ensureDefaultCryptoInFavorites() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MarketPulse") },
                actions = {
                    TextButton(onClick = onOpenSearch) { Text("Поиск") }
                    TextButton(onClick = onOpenSettings) { Text("Настройки") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            if (state.isOffline) OfflineBanner()

            when {
                state.isLoading -> LoadingState()
                state.error != null && state.favoritesQuotes.isEmpty() && state.fiatRates.isEmpty() ->
                    ErrorState(message = state.error ?: "Ошибка", onRetry = { vm.onEvent(HomeEvent.OnRetry) })
                state.favoritesQuotes.isEmpty() && state.fiatRates.isEmpty() ->
                    EmptyState(
                        title = "Данных нет. Подключись к интернету и обнови.",
                        actionText = "Обновить",
                        onAction = { vm.onEvent(HomeEvent.OnRefresh) }
                    )
                else -> Content(state, onOpenDetail, onRefresh = { vm.onEvent(HomeEvent.OnRefresh) })
            }
        }
    }
}

@Composable
private fun Content(
    state: HomeUiState,
    onOpenDetail: (AssetType, String) -> Unit,
    onRefresh: () -> Unit
) {
    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.End) {
        if (state.isRefreshing) CircularProgressIndicator(modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Button(onClick = onRefresh) { Text("Обновить") }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Избранное", style = MaterialTheme.typography.titleMedium)
        }
        items(state.favoritesQuotes, key = { it.type.name + "_" + it.symbol }) { q ->
            QuoteCard(
                quote = q,
                isFavorite = true,
                onToggleFavorite = { /* в деталях */ },
                onOpen = { onOpenDetail(q.type, q.symbol) }
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
        item {
            Text("Фиат", style = MaterialTheme.typography.titleMedium)
        }
        items(state.fiatRates, key = { it.base + "_" + it.target }) { r ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${r.base.uppercase()} → ${r.target.uppercase()}")
                    Text("${r.rate}")
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

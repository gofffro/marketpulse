@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketpulse.presentation.screen_search

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
import com.example.marketpulse.presentation.components.ErrorState
import com.example.marketpulse.presentation.components.LoadingState
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun SearchScreen(
    container: AppContainer,
    onBack: () -> Unit,
    onOpenDetail: (AssetType, String) -> Unit
) {
    val vm = remember { SearchViewModel(container.marketRepository) }
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Тикер или название") }
            )
            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> LoadingState(modifier = Modifier.fillMaxSize())
                state.error != null -> ErrorState(message = state.error ?: "Ошибка") { vm.onQueryChange(state.query) }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.results, key = { it.symbol }) { a ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(a.symbol, style = MaterialTheme.typography.titleMedium)
                                    Text(a.name, style = MaterialTheme.typography.bodySmall)
                                }
                                Row {
                                    TextButton(onClick = { vm.toggleFavorite(a) }) { Text("⭐") }
                                    TextButton(onClick = { onOpenDetail(a.type, a.symbol) }) { Text("Детали") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

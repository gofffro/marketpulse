@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketpulse.presentation.screen_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.marketpulse.core.util.AppContainer
import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.presentation.components.ErrorState
import com.example.marketpulse.presentation.components.OfflineBanner
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun DetailScreen(
    container: AppContainer,
    type: AssetType,
    symbol: String,
    onBack: () -> Unit
) {
    val vm = remember { DetailViewModel(container.marketRepository, symbol, type) }
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали: $symbol") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } },
                actions = {
                    TextButton(onClick = vm::toggleFavorite) { Text(if (state.isFavorite) "★" else "☆") }
                    TextButton(onClick = { vm.refresh(force = true) }) { Text("Обновить") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {
            if (state.isOffline) OfflineBanner()

            if (state.error != null && state.quote == null) {
                ErrorState(message = state.error ?: "Ошибка", onRetry = { vm.refresh(force = true) })
                return@Column
            }

            val q = state.quote
            if (q == null) {
                Text("Нет данных. Добавь в избранное и обнови.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = vm::toggleFavorite) { Text("⭐ В избранное") }
                return@Column
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("$symbol (${type.name})", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Цена: ${q.price}")
                    Text("Изменение: ${q.change} (${q.changePercent}%)")
                    Spacer(Modifier.height(8.dp))
                    Text("Обновлено: ${q.updatedAtMillis}")
                }
            }
        }
    }
}

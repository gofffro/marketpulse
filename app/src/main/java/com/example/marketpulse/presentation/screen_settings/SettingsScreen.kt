@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketpulse.presentation.screen_settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.marketpulse.core.util.AppContainer
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun SettingsScreen(
    container: AppContainer,
    onBack: () -> Unit
) {
    val vm = remember { SettingsViewModel(container.settingsDataStore(), container.marketRepository) }
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.baseFiat,
                onValueChange = vm::setBaseFiat,
                label = { Text("Базовая валюта (eur/usd/...)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.cacheTtlMinutes.toString(),
                onValueChange = { txt -> txt.toIntOrNull()?.let(vm::setTtl) },
                label = { Text("TTL кэша (мин)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Автообновление при старте")
                Switch(checked = state.autoRefresh, onCheckedChange = vm::setAuto)
            }

            Button(onClick = vm::clearCache) { Text("Обновить данные (force)") }
        }
    }
}

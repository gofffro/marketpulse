package com.example.marketpulse.presentation.screen_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.marketpulse.core.util.AppContainer

@Composable
fun SplashScreen(
    container: AppContainer,
    onGoHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        // простая “инициализация”
        onGoHome()
    }
}

package com.example.marketpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.marketpulse.core.util.AppContainer
import com.example.marketpulse.presentation.navigation.AppNavGraph
import com.example.marketpulse.ui.theme.MarketPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val container = remember { AppContainer(applicationContext) }

            MarketPulseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    AppNavGraph(container = container, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

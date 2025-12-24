package com.example.marketpulse.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketpulse.domain.model.Quote
import kotlin.math.abs

@Composable
fun QuoteCard(
    quote: Quote,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(quote.symbol, style = MaterialTheme.typography.titleMedium)
                Text("Цена: ${quote.price}", style = MaterialTheme.typography.bodyMedium)
                val sign = if (quote.change >= 0) "+" else "-"
                Text(
                    "День: $sign${abs(quote.change)} (${sign}${abs(quote.changePercent)}%)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onToggleFavorite) {
                Text(if (isFavorite) "★" else "☆", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

package com.example.marketpulse.domain.repository

import com.example.marketpulse.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun observeFavorites(): Flow<List<Asset>>
    fun observeQuotesForFavorites(): Flow<List<Quote>>
    fun observeFiatRates(base: String, targets: List<String>): Flow<List<FiatRate>>

    suspend fun toggleFavorite(asset: Asset)
    suspend fun refreshAll(force: Boolean)

    suspend fun refreshFavoritesQuotes(force: Boolean)
    suspend fun refreshFiat(base: String, targets: List<String>, force: Boolean)

    suspend fun searchAssets(query: String): List<Asset>

    fun observeIsOffline(): Flow<Boolean>
}

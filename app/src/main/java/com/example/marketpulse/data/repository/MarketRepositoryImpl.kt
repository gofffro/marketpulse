package com.example.marketpulse.data.repository

import com.example.marketpulse.BuildConfig
import com.example.marketpulse.core.network.ApiErrorMapper
import com.example.marketpulse.core.util.DispatchersProvider
import com.example.marketpulse.core.util.Result
import com.example.marketpulse.core.util.SettingsDataStore
import com.example.marketpulse.core.util.TimeProvider
import com.example.marketpulse.data.local.dao.*
import com.example.marketpulse.data.local.entity.MetadataEntity
import com.example.marketpulse.data.mapper.*
import com.example.marketpulse.data.remote.api.CurrencyApi
import com.example.marketpulse.data.remote.api.FinnhubApi
import com.example.marketpulse.domain.model.*
import com.example.marketpulse.domain.repository.MarketRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class MarketRepositoryImpl(
    private val finnhubApi: FinnhubApi,
    private val currencyApiPrimary: CurrencyApi,
    private val currencyApiFallback: CurrencyApi,
    private val favoritesDao: FavoritesDao,
    private val quotesDao: QuotesDao,
    private val fiatDao: FiatRatesDao,
    private val metadataDao: MetadataDao,
    private val settings: SettingsDataStore,
    private val dispatchers: DispatchersProvider,
    private val time: TimeProvider
) : MarketRepository {

    private val offline = MutableStateFlow(false)
    private val isRefreshingNow = AtomicBoolean(false)

    override fun observeIsOffline(): Flow<Boolean> = offline.asStateFlow()

    override fun observeFavorites(): Flow<List<Asset>> =
        favoritesDao.observeFavorites().map { list -> list.map { it.toDomain() } }

    override fun observeQuotesForFavorites(): Flow<List<Quote>> =
        favoritesDao.observeFavorites()
            .map { favs -> favs.map { "${it.type}_${it.symbol}" } }
            .distinctUntilChanged()
            .flatMapLatest { keys ->
                if (keys.isEmpty()) flowOf(emptyList())
                else quotesDao.observeByKeys(keys).map { list -> list.map { it.toDomain() } }
            }

    override fun observeFiatRates(base: String, targets: List<String>): Flow<List<FiatRate>> {
        val keys = targets.map { "${base.lowercase()}_${it.lowercase()}" }
        return if (keys.isEmpty()) flowOf(emptyList())
        else fiatDao.observeByKeys(keys).map { it.map { e -> e.toDomain() } }
    }

    override suspend fun toggleFavorite(asset: Asset) = withContext(dispatchers.io) {
        val now = time.nowMillis()
        if (favoritesDao.exists(asset.symbol)) favoritesDao.delete(asset.symbol)
        else favoritesDao.upsert(asset.toFavoriteEntity(now))
    }

    override suspend fun refreshAll(force: Boolean) {
        val base = settings.baseFiat.first()
        val targets = listOf("usd", "rub", "jpy")
        refreshFavoritesQuotes(force)
        refreshFiat(base, targets, force)
    }

    private suspend fun shouldRefresh(metaKey: String, ttlMinutes: Int, now: Long): Boolean {
        val last = metadataDao.get(metaKey)?.valueLong ?: 0L
        val ttlMillis = ttlMinutes * 60_000L
        return (now - last) > ttlMillis
    }

    private suspend fun markRefreshed(metaKey: String, now: Long) {
        metadataDao.put(MetadataEntity(key = metaKey, valueLong = now))
    }

    override suspend fun refreshFavoritesQuotes(force: Boolean) = withContext(dispatchers.io) {
        if (!isRefreshingNow.compareAndSet(false, true)) return@withContext
        try {
            val now = time.nowMillis()
            val ttl = settings.cacheTtlMinutes.first()
            val metaKey = "favorites_quotes_last_refresh"
            val doRefresh = force || shouldRefresh(metaKey, ttl, now)
            if (!doRefresh) return@withContext

            val favorites = favoritesDao.getAll()
            if (favorites.isEmpty()) return@withContext

            val token = BuildConfig.FINNHUB_TOKEN
            val quotes = favorites.map { fav ->
                val dto = finnhubApi.getQuote(symbol = fav.symbol, token = token)
                dto.toDomain(symbol = fav.symbol, type = AssetType.valueOf(fav.type), now = now)
            }

            quotesDao.upsertAll(quotes.map { it.toEntity() })
            markRefreshed(metaKey, now)
            offline.value = false
        } catch (t: Throwable) {
            val (msg, _) = ApiErrorMapper.toMessage(t)
            offline.value = msg.contains("сети", ignoreCase = true)
            // Ничего не чистим: кэш остаётся
        } finally {
            isRefreshingNow.set(false)
        }
    }

    override suspend fun refreshFiat(base: String, targets: List<String>, force: Boolean) =
        withContext(dispatchers.io) {
            try {
                val now = time.nowMillis()
                val ttl = settings.cacheTtlMinutes.first()
                val metaKey = "fiat_${base.lowercase()}_last_refresh"
                val doRefresh = force || shouldRefresh(metaKey, ttl, now)
                if (!doRefresh) return@withContext

                val map = fetchCurrencyRatesWithFallback(base.lowercase())
                val rates = targets.mapNotNull { tgt ->
                    val rate = map[tgt.lowercase()] as? Double
                    rate?.let {
                        FiatRate(base = base.lowercase(), target = tgt.lowercase(), rate = it, updatedAtMillis = now)
                    }
                }

                fiatDao.upsertAll(rates.map { it.toEntity() })
                markRefreshed(metaKey, now)
                offline.value = false
            } catch (t: Throwable) {
                val (msg, _) = ApiErrorMapper.toMessage(t)
                offline.value = msg.contains("сети", ignoreCase = true)
            }
        }

    /**
     * currency-api primary: jsdelivr
     * fallback: pages.dev
     */
    private suspend fun fetchCurrencyRatesWithFallback(base: String): Map<String, Any> {
        return try {
            val resp = currencyApiPrimary.getRates(base)
            extractRatesMap(resp, base)
        } catch (_: Throwable) {
            val resp = currencyApiFallback.getRates(base)
            extractRatesMap(resp, base)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractRatesMap(resp: Map<String, Any>, base: String): Map<String, Any> {
        val baseObj = resp[base] ?: return emptyMap()
        return baseObj as? Map<String, Any> ?: emptyMap()
    }

    override suspend fun searchAssets(query: String): List<Asset> = withContext(dispatchers.io) {
        if (query.isBlank()) return@withContext emptyList()
        val token = BuildConfig.FINNHUB_TOKEN
        val dto = finnhubApi.search(query = query, exchange = "US", token = token)
        val items = dto.result.orEmpty()

        items.take(20).mapNotNull { it ->
            val symbol = it.symbol?.trim().orEmpty()
            val name = it.description?.trim().orEmpty()
            if (symbol.isBlank()) return@mapNotNull null
            Asset(symbol = symbol, name = if (name.isBlank()) symbol else name, type = AssetType.STOCK)
        }
    }
}

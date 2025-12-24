package com.example.marketpulse.core.util

import android.content.Context
import androidx.room.Room
import com.example.marketpulse.core.db.AppDatabase
import com.example.marketpulse.core.network.RetrofitFactory
import com.example.marketpulse.data.remote.api.CurrencyApi
import com.example.marketpulse.data.remote.api.FinnhubApi
import com.example.marketpulse.data.repository.MarketRepositoryImpl
import com.example.marketpulse.domain.repository.MarketRepository

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "marketpulse.db"
    ).build()

    private val okHttp = RetrofitFactory.defaultOkHttp()

    private val finnhubRetrofit = RetrofitFactory.create(
        baseUrl = "https://finnhub.io/api/v1/",
        client = okHttp
    )

    private val currencyPrimaryRetrofit = RetrofitFactory.create(
        baseUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/",
        client = okHttp
    )

    private val currencyFallbackRetrofit = RetrofitFactory.create(
        baseUrl = "https://latest.currency-api.pages.dev/v1/",
        client = okHttp
    )

    private val finnhubApi: FinnhubApi = finnhubRetrofit.create(FinnhubApi::class.java)
    private val currencyPrimaryApi: CurrencyApi = currencyPrimaryRetrofit.create(CurrencyApi::class.java)
    private val currencyFallbackApi: CurrencyApi = currencyFallbackRetrofit.create(CurrencyApi::class.java)

    private val settings = SettingsDataStore(context)
    private val dispatchers = DefaultDispatchers
    private val time = SystemTimeProvider

    val marketRepository: MarketRepository = MarketRepositoryImpl(
        finnhubApi = finnhubApi,
        currencyApiPrimary = currencyPrimaryApi,
        currencyApiFallback = currencyFallbackApi,
        favoritesDao = db.favoritesDao(),
        quotesDao = db.quotesDao(),
        fiatDao = db.fiatDao(),
        metadataDao = db.metadataDao(),
        settings = settings,
        dispatchers = dispatchers,
        time = time
    )

    fun settingsDataStore(): SettingsDataStore = settings
}

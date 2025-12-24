package com.example.marketpulse.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * currency-api: /v1/currencies/{base}.json
 * базовые URL:
 * - https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/  :contentReference[oaicite:2]{index=2}
 * - https://latest.currency-api.pages.dev/v1/ (fallback)              :contentReference[oaicite:3]{index=3}
 */
interface CurrencyApi {
    @GET("currencies/{base}.json")
    suspend fun getRates(@Path("base") base: String): Map<String, Any>
}

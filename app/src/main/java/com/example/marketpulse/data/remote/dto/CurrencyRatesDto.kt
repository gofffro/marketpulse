package com.example.marketpulse.data.remote.dto

import com.squareup.moshi.Json

/**
 * currency-api: https://.../currencies/eur.json
 * формат: { "date":"2024-03-06", "eur": { "usd":1.08, ... } }
 */
data class CurrencyRatesDto(
    val date: String?,
    @Json(name = "eur") val eur: Map<String, Double>?,
    @Json(name = "usd") val usd: Map<String, Double>?,
    @Json(name = "rub") val rub: Map<String, Double>?,
    @Json(name = "jpy") val jpy: Map<String, Double>?
    // Мы будем парсить динамически через Map-ответ, поэтому ниже сделаем второй интерфейс.
)

package com.example.marketpulse.data.remote.dto

import com.squareup.moshi.Json

data class FinnhubQuoteDto(
    @Json(name = "c") val current: Double?,      // current price
    @Json(name = "d") val change: Double?,       // change
    @Json(name = "dp") val changePercent: Double?, // percent change
    @Json(name = "h") val high: Double?,
    @Json(name = "l") val low: Double?,
    @Json(name = "o") val open: Double?,
    @Json(name = "pc") val prevClose: Double?
)

package com.example.marketpulse.domain.model

data class Quote(
    val symbol: String,
    val type: AssetType,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val updatedAtMillis: Long
)

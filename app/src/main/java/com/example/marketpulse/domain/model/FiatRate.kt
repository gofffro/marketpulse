package com.example.marketpulse.domain.model

data class FiatRate(
    val base: String,
    val target: String,
    val rate: Double,
    val updatedAtMillis: Long
)

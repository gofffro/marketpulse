package com.example.marketpulse.domain.model

data class Asset(
    val symbol: String,
    val name: String,
    val type: AssetType
)

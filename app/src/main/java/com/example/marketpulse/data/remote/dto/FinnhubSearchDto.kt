package com.example.marketpulse.data.remote.dto

data class FinnhubSearchDto(
    val count: Int?,
    val result: List<FinnhubSearchItemDto>?
)

data class FinnhubSearchItemDto(
    val description: String?,
    val displaySymbol: String?,
    val symbol: String?,
    val type: String?
)

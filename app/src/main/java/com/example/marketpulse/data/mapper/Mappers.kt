package com.example.marketpulse.data.mapper

import com.example.marketpulse.data.local.entity.*
import com.example.marketpulse.data.remote.dto.FinnhubQuoteDto
import com.example.marketpulse.domain.model.*

fun FavoriteEntity.toDomain(): Asset =
    Asset(symbol = symbol, name = name, type = AssetType.valueOf(type))

fun Asset.toFavoriteEntity(now: Long): FavoriteEntity =
    FavoriteEntity(symbol = symbol, name = name, type = type.name, addedAtMillis = now)

fun QuoteEntity.toDomain(): Quote =
    Quote(
        symbol = symbol,
        type = AssetType.valueOf(type),
        price = price,
        change = change,
        changePercent = changePercent,
        updatedAtMillis = updatedAtMillis
    )

fun Quote.toEntity(): QuoteEntity =
    QuoteEntity(
        key = "${type.name}_${symbol}",
        symbol = symbol,
        type = type.name,
        price = price,
        change = change,
        changePercent = changePercent,
        updatedAtMillis = updatedAtMillis
    )

fun FiatRateEntity.toDomain(): FiatRate =
    FiatRate(base = base, target = target, rate = rate, updatedAtMillis = updatedAtMillis)

fun FiatRate.toEntity(): FiatRateEntity =
    FiatRateEntity(
        key = "${base}_${target}",
        base = base,
        target = target,
        rate = rate,
        updatedAtMillis = updatedAtMillis
    )

fun FinnhubQuoteDto.toDomain(symbol: String, type: AssetType, now: Long): Quote {
    val p = current ?: 0.0
    val ch = change ?: 0.0
    val chp = changePercent ?: 0.0
    return Quote(
        symbol = symbol,
        type = type,
        price = p,
        change = ch,
        changePercent = chp,
        updatedAtMillis = now
    )
}

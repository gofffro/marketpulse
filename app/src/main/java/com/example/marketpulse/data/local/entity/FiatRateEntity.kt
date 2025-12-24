package com.example.marketpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fiat_rates")
data class FiatRateEntity(
    @PrimaryKey val key: String, // "${base}_${target}"
    val base: String,
    val target: String,
    val rate: Double,
    val updatedAtMillis: Long
)

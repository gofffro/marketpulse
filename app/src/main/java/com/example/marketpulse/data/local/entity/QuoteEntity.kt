package com.example.marketpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val key: String, // "${type}_${symbol}"
    val symbol: String,
    val type: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val updatedAtMillis: Long
)

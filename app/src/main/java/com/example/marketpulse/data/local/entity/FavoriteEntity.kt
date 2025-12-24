package com.example.marketpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val type: String, // STOCK/CRYPTO
    val addedAtMillis: Long
)

package com.example.marketpulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetadataEntity(
    @PrimaryKey val key: String, // например "favorites_quotes_last_refresh"
    val valueLong: Long
)

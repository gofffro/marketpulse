package com.example.marketpulse.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marketpulse.data.local.dao.*
import com.example.marketpulse.data.local.entity.*

@Database(
    entities = [
        FavoriteEntity::class,
        QuoteEntity::class,
        FiatRateEntity::class,
        MetadataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun quotesDao(): QuotesDao
    abstract fun fiatDao(): FiatRatesDao
    abstract fun metadataDao(): MetadataDao
}

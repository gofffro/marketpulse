package com.example.marketpulse.data.local.dao

import androidx.room.*
import com.example.marketpulse.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuotesDao {
    @Query("SELECT * FROM quotes WHERE key IN (:keys)")
    fun observeByKeys(keys: List<String>): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE key IN (:keys)")
    suspend fun getByKeys(keys: List<String>): List<QuoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<QuoteEntity>)

    @Query("DELETE FROM quotes")
    suspend fun clear()
}

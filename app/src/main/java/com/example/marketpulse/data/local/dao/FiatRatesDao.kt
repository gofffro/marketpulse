package com.example.marketpulse.data.local.dao

import androidx.room.*
import com.example.marketpulse.data.local.entity.FiatRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FiatRatesDao {
    @Query("SELECT * FROM fiat_rates WHERE key IN (:keys)")
    fun observeByKeys(keys: List<String>): Flow<List<FiatRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<FiatRateEntity>)

    @Query("DELETE FROM fiat_rates")
    suspend fun clear()
}

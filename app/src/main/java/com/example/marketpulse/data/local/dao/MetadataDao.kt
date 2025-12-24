package com.example.marketpulse.data.local.dao

import androidx.room.*
import com.example.marketpulse.data.local.entity.MetadataEntity

@Dao
interface MetadataDao {
    @Query("SELECT * FROM metadata WHERE key = :key LIMIT 1")
    suspend fun get(key: String): MetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(entity: MetadataEntity)

    @Query("DELETE FROM metadata")
    suspend fun clear()
}

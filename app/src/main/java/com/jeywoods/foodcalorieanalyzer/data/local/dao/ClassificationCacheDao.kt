package com.jeywoods.foodcalorieanalyzer.data.local.dao

import androidx.room.*
import com.jeywoods.foodcalorieanalyzer.data.local.entity.ClassificationCacheEntity

@Dao
interface ClassificationCacheDao {
    @Query("SELECT * FROM classification_cache WHERE imageHash = :hash")
    suspend fun getCacheByHash(hash: String): ClassificationCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: ClassificationCacheEntity)

    @Query("DELETE FROM classification_cache WHERE timestamp < :expiryTime")
    suspend fun deleteOldCache(expiryTime: Long)

    @Query("DELETE FROM classification_cache")
    suspend fun deleteAll()
}
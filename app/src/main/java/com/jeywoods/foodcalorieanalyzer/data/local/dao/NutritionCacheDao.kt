package com.jeywoods.foodcalorieanalyzer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeywoods.foodcalorieanalyzer.data.local.entity.NutritionCacheEntity

@Dao
interface NutritionCacheDao {
    @Query("SELECT * FROM nutrition_cache WHERE foodName = :name")
    suspend fun get(name: String): NutritionCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NutritionCacheEntity)

    @Query("DELETE FROM nutrition_cache WHERE timestamp < :expiryTime")
    suspend fun deleteOld(expiryTime: Long)
}
package com.jeywoods.foodcalorieanalyzer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeywoods.foodcalorieanalyzer.data.local.dao.ClassificationCacheDao
import com.jeywoods.foodcalorieanalyzer.data.local.dao.MealDao
import com.jeywoods.foodcalorieanalyzer.data.local.dao.NutritionCacheDao
import com.jeywoods.foodcalorieanalyzer.data.local.entity.ClassificationCacheEntity
import com.jeywoods.foodcalorieanalyzer.data.local.entity.MealEntity
import com.jeywoods.foodcalorieanalyzer.data.local.entity.NutritionCacheEntity

@Database(
    entities = [
        MealEntity::class,
        ClassificationCacheEntity::class,
        NutritionCacheEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun classificationCacheDao(): ClassificationCacheDao
    abstract fun nutritionCacheDao(): NutritionCacheDao
}
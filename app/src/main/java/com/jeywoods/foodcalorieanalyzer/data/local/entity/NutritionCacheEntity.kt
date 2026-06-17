package com.jeywoods.foodcalorieanalyzer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_cache")
data class NutritionCacheEntity(
    @PrimaryKey val foodName: String,
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val fatPer100g: Float,
    val saturatedFatPer100g: Float,
    val carbsPer100g: Float,
    val fiberPer100g: Float,
    val sugarPer100g: Float,
    val sodiumPer100g: Float,
    val potassiumPer100g: Float,
    val cholesterolPer100g: Float,
    val timestamp: Long = System.currentTimeMillis()
)
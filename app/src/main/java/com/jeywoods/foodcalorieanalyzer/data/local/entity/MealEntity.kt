package com.jeywoods.foodcalorieanalyzer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodItemId: Int,
    val englishName: String,
    val russianName: String,
    val grams: Float,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val date: Date = Date()
)
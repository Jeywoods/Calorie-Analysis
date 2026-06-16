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
    val saturatedFat: Float = 0f,
    val carbs: Float,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f,
    val potassium: Float = 0f,
    val cholesterol: Float = 0f,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val date: Date = Date()
)
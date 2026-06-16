package com.jeywoods.foodcalorieanalyzer.domain.model

data class Meal(
    val id: Long = 0,
    val foodItem: FoodItem,
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
    val timestamp: Long = System.currentTimeMillis()
)
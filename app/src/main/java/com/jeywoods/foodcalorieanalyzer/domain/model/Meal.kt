package com.jeywoods.foodcalorieanalyzer.domain.model

data class Meal(
    val id: Long = 0,
    val foodItem: FoodItem,
    val grams: Float,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis()
) {
    val nutritionInfo: NutritionInfo
        get() = NutritionInfo(calories, protein, fat, carbs, grams)
}
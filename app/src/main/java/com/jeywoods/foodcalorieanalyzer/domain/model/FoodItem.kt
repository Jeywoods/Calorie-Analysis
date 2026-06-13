package com.jeywoods.foodcalorieanalyzer.domain.model

data class FoodItem(
    val id: Int,
    val englishName: String,
    val russianName: String,
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val fatPer100g: Float,
    val carbsPer100g: Float
) {
    fun calculateNutrition(grams: Float): NutritionInfo {
        val multiplier = grams / 100f
        return NutritionInfo(
            calories = caloriesPer100g * multiplier,
            protein = proteinPer100g * multiplier,
            fat = fatPer100g * multiplier,
            carbs = carbsPer100g * multiplier,
            grams = grams
        )
    }
}

data class NutritionInfo(
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val grams: Float
)
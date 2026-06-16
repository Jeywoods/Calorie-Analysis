package com.jeywoods.foodcalorieanalyzer.domain.model

data class FoodItem(
    val id: Int,
    val englishName: String,
    val russianName: String,
    val caloriesPer100g: Float = 0f,
    val proteinPer100g: Float = 0f,
    val fatPer100g: Float = 0f,
    val saturatedFatPer100g: Float = 0f,
    val carbsPer100g: Float = 0f,
    val fiberPer100g: Float = 0f,
    val sugarPer100g: Float = 0f,
    val sodiumPer100g: Float = 0f,
    val potassiumPer100g: Float = 0f,
    val cholesterolPer100g: Float = 0f
) {
    fun calculateNutrition(grams: Float): NutritionInfo {
        val multiplier = grams / 100f
        return NutritionInfo(
            calories = caloriesPer100g * multiplier,
            protein = proteinPer100g * multiplier,
            fat = fatPer100g * multiplier,
            saturatedFat = saturatedFatPer100g * multiplier,
            carbs = carbsPer100g * multiplier,
            fiber = fiberPer100g * multiplier,
            sugar = sugarPer100g * multiplier,
            sodium = sodiumPer100g * multiplier,
            potassium = potassiumPer100g * multiplier,
            cholesterol = cholesterolPer100g * multiplier,
            grams = grams
        )
    }

    val hasNutritionData: Boolean
        get() = caloriesPer100g > 0
}

data class NutritionInfo(
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
    val grams: Float
)
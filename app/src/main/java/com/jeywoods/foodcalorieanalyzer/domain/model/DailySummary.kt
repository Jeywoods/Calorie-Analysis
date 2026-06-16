package com.jeywoods.foodcalorieanalyzer.domain.model

data class DailySummary(
    val date: String,
    val meals: List<Meal>,
    val totalCalories: Float,
    val totalProtein: Float,
    val totalFat: Float,
    val totalSaturatedFat: Float = 0f,
    val totalCarbs: Float,
    val totalFiber: Float = 0f,
    val totalSugar: Float = 0f,
    val totalSodium: Float = 0f,
    val totalPotassium: Float = 0f,
    val totalCholesterol: Float = 0f
) {
    val mealCount: Int get() = meals.size

    companion object {
        fun calculate(date: String, meals: List<Meal>): DailySummary {
            return DailySummary(
                date = date,
                meals = meals,
                totalCalories = meals.sumOf { it.calories.toDouble() }.toFloat(),
                totalProtein = meals.sumOf { it.protein.toDouble() }.toFloat(),
                totalFat = meals.sumOf { it.fat.toDouble() }.toFloat(),
                totalSaturatedFat = meals.sumOf { it.saturatedFat.toDouble() }.toFloat(),
                totalCarbs = meals.sumOf { it.carbs.toDouble() }.toFloat(),
                totalFiber = meals.sumOf { it.fiber.toDouble() }.toFloat(),
                totalSugar = meals.sumOf { it.sugar.toDouble() }.toFloat(),
                totalSodium = meals.sumOf { it.sodium.toDouble() }.toFloat(),
                totalPotassium = meals.sumOf { it.potassium.toDouble() }.toFloat(),
                totalCholesterol = meals.sumOf { it.cholesterol.toDouble() }.toFloat()
            )
        }
    }
}
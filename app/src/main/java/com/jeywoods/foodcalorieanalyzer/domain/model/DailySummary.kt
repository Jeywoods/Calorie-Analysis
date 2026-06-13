package com.jeywoods.foodcalorieanalyzer.domain.model

data class DailySummary(
    val date: String, // Format: "yyyy-MM-dd"
    val meals: List<Meal>,
    val totalCalories: Float,
    val totalProtein: Float,
    val totalFat: Float,
    val totalCarbs: Float
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
                totalCarbs = meals.sumOf { it.carbs.toDouble() }.toFloat()
            )
        }
    }
}
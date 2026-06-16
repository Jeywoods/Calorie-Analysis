package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import javax.inject.Inject

class AddMealUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        foodItem: FoodItem,
        grams: Float,
        imagePath: String?
    ): Long {
        val nutrition = foodItem.calculateNutrition(grams)

        val meal = Meal(
            foodItem = foodItem,
            grams = grams,
            calories = nutrition.calories,
            protein = nutrition.protein,
            fat = nutrition.fat,
            saturatedFat = nutrition.saturatedFat,
            carbs = nutrition.carbs,
            fiber = nutrition.fiber,
            sugar = nutrition.sugar,
            sodium = nutrition.sodium,
            potassium = nutrition.potassium,
            cholesterol = nutrition.cholesterol,
            imagePath = imagePath,
            timestamp = System.currentTimeMillis()
        )

        return mealRepository.addMeal(meal)
    }
}
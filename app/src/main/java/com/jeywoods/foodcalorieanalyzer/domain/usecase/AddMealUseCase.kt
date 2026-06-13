package com.jeywoods.foodcalorieanalyzer.domain.usecase

import android.util.Log
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
        Log.d("AddMealUC", "=== Adding meal ===")
        Log.d("AddMealUC", "Food: ${foodItem.russianName} (id=${foodItem.id})")
        Log.d("AddMealUC", "Grams: $grams")
        Log.d("AddMealUC", "ImagePath: $imagePath")

        val nutrition = foodItem.calculateNutrition(grams)
        Log.d("AddMealUC", "Calculated: ${nutrition.calories}kcal, P:${nutrition.protein}, F:${nutrition.fat}, C:${nutrition.carbs}")

        val meal = Meal(
            foodItem = foodItem,
            grams = grams,
            calories = nutrition.calories,
            protein = nutrition.protein,
            fat = nutrition.fat,
            carbs = nutrition.carbs,
            imagePath = imagePath,
            timestamp = System.currentTimeMillis()
        )

        Log.d("AddMealUC", "Calling mealRepository.addMeal()")
        val mealId = mealRepository.addMeal(meal)
        Log.d("AddMealUC", "Meal saved with ID: $mealId")

        return mealId
    }
}
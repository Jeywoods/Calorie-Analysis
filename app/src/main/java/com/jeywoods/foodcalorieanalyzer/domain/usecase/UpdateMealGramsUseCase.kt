package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import javax.inject.Inject

class UpdateMealGramsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(mealId: Long, grams: Float) {
        if (grams <= 0) return

        val currentMeal = mealRepository.getMealById(mealId) ?: return
        val multiplier = grams / currentMeal.grams

        mealRepository.updateMeal(
            currentMeal.copy(
                grams = grams,
                calories = currentMeal.calories * multiplier,
                protein = currentMeal.protein * multiplier,
                fat = currentMeal.fat * multiplier,
                saturatedFat = currentMeal.saturatedFat * multiplier,
                carbs = currentMeal.carbs * multiplier,
                fiber = currentMeal.fiber * multiplier,
                sugar = currentMeal.sugar * multiplier,
                sodium = currentMeal.sodium * multiplier,
                potassium = currentMeal.potassium * multiplier,
                cholesterol = currentMeal.cholesterol * multiplier
            )
        )
    }
}
package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import javax.inject.Inject

class UpdateMealGramsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(mealId: Long, grams: Float) {
        require(grams > 0) { "Граммы должны быть больше нуля" }
        mealRepository.updateMealGrams(mealId, grams)
    }
}
package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import javax.inject.Inject

class DeleteMealUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(mealId: Long) {
        mealRepository.deleteMeal(mealId)
    }
}
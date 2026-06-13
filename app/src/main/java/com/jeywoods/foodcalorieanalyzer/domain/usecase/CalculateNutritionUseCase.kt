package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.model.NutritionInfo
import javax.inject.Inject

class CalculateNutritionUseCase @Inject constructor() {

    operator fun invoke(foodItem: FoodItem, grams: Float): NutritionInfo {
        require(grams > 0) { "Граммы должны быть больше нуля" }
        return foodItem.calculateNutrition(grams)
    }
}
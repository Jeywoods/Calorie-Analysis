package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.repository.FoodRepository
import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(query: String): List<FoodItem> {
        return foodRepository.searchFoods(query)
    }
}
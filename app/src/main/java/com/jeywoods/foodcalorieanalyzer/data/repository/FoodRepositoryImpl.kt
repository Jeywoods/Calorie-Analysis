package com.jeywoods.foodcalorieanalyzer.data.repository

import com.jeywoods.foodcalorieanalyzer.data.ml.Food101Metadata
import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.repository.FoodRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor() : FoodRepository {

    override fun getAllFoods(): List<FoodItem> = Food101Metadata.getAllFoods()

    override fun getFoodById(id: Int): FoodItem? = Food101Metadata.getFoodById(id)

    override fun getFoodByEnglishName(name: String): FoodItem? = Food101Metadata.getFoodByEnglishName(name)

    override fun searchFoods(query: String): List<FoodItem> = Food101Metadata.searchFoods(query)

    override fun getFoodCount(): Int = Food101Metadata.getFoodCount()
}
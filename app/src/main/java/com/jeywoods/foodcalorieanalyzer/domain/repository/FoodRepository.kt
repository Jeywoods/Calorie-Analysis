package com.jeywoods.foodcalorieanalyzer.domain.repository

import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem

interface FoodRepository {
    fun getAllFoods(): List<FoodItem>
    fun getFoodById(id: Int): FoodItem?
    fun getFoodByEnglishName(name: String): FoodItem?
    fun searchFoods(query: String): List<FoodItem>
    fun getFoodCount(): Int
}
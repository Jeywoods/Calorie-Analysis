package com.jeywoods.foodcalorieanalyzer.domain.repository

import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MealRepository {
    fun getMealsByDate(date: Date): Flow<List<Meal>>
    fun getMealsByDateRange(startDate: Date, endDate: Date): Flow<List<Meal>>
    suspend fun addMeal(meal: Meal): Long
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(mealId: Long)
    fun getDailySummary(date: Date): Flow<DailySummary>
    suspend fun getMealById(id: Long): Meal?
}
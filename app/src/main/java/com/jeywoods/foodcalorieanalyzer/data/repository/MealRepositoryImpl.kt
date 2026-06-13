package com.jeywoods.foodcalorieanalyzer.data.repository

import com.jeywoods.foodcalorieanalyzer.data.local.dao.MealDao
import com.jeywoods.foodcalorieanalyzer.data.mapper.MealMapper
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepositoryImpl @Inject constructor(
    private val mealDao: MealDao
) : MealRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun getMealsByDate(date: Date): Flow<List<Meal>> {
        return mealDao.getMealsByDate(date)
            .map { entities -> MealMapper.toDomainList(entities) }
    }

    override fun getMealsByDateRange(startDate: Date, endDate: Date): Flow<List<Meal>> {
        return mealDao.getMealsByDateRange(startDate, endDate)
            .map { entities -> MealMapper.toDomainList(entities) }
    }

    override suspend fun addMeal(meal: Meal): Long {
        val entity = MealMapper.toEntity(meal)
        return mealDao.insertMeal(entity)
    }

    override suspend fun updateMealGrams(mealId: Long, grams: Float) {
        val entity = mealDao.getMealById(mealId) ?: return
        val meal = MealMapper.toDomain(entity)
        val nutrition = meal.foodItem.calculateNutrition(grams)

        val updatedEntity = entity.copy(
            grams = grams,
            calories = nutrition.calories,
            protein = nutrition.protein,
            fat = nutrition.fat,
            carbs = nutrition.carbs
        )

        mealDao.updateMeal(updatedEntity)
    }

    override suspend fun deleteMeal(mealId: Long) {
        mealDao.deleteMealById(mealId)
    }

    override fun getDailySummary(date: Date): Flow<DailySummary> {
        val dateString = dateFormat.format(date)
        return getMealsByDate(date).map { meals ->
            DailySummary.calculate(dateString, meals)
        }
    }

    override suspend fun getMealById(id: Long): Meal? {
        val entity = mealDao.getMealById(id) ?: return null
        return MealMapper.toDomain(entity)
    }
}
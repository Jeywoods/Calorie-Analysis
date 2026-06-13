package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetDailyMealsUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    operator fun invoke(): Flow<List<Meal>> {
        val today = getTodayDate()
        return mealRepository.getMealsByDate(today)
    }

    operator fun invoke(date: Date): Flow<List<Meal>> {
        return mealRepository.getMealsByDate(date)
    }

    fun getDailySummary(): Flow<DailySummary> {
        val today = getTodayDate()
        return mealRepository.getDailySummary(today)
    }

    fun getDailySummary(date: Date): Flow<DailySummary> {
        return mealRepository.getDailySummary(date)
    }

    private fun getTodayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}
package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    operator fun invoke(): Flow<List<DailySummary>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startDate = calendar.time

        return mealRepository.getMealsByDateRange(startDate, endDate)
            .map { meals ->
                meals.groupBy { meal ->
                    val mealCalendar = Calendar.getInstance()
                    mealCalendar.timeInMillis = meal.timestamp
                    dateFormat.format(mealCalendar.time)
                }.map { (date, dayMeals) ->
                    DailySummary.calculate(date, dayMeals)
                }.sortedByDescending { it.date }
            }
    }
}
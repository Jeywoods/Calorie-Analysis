package com.jeywoods.foodcalorieanalyzer.domain.usecase

import android.content.Context
import android.os.Environment
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ExportHistoryUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mealRepository: MealRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

    suspend operator fun invoke(): File = withContext(Dispatchers.IO) {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startDate = calendar.time

        val meals = mealRepository.getMealsByDateRange(startDate, endDate).first()

        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val fileName = "food_diary_${dateFormat.format(Date())}.csv"
        val file = File(downloadsDir, fileName)

        FileWriter(file).use { writer ->
            writer.write("Дата,Блюдо (RU),Граммы,Калории,Белки,Жиры,Углеводы,Время\n")

            meals.sortedByDescending { it.timestamp }.forEach { meal ->
                val mealDate = Calendar.getInstance().apply {
                    timeInMillis = meal.timestamp
                }

                writer.write(
                    "${dateFormat.format(mealDate.time)}," +
                            "${meal.foodItem.russianName}," +
                            "${meal.grams}г," +
                            "${String.format("%.1f", meal.calories)}," +
                            "${String.format("%.1f", meal.protein)}," +
                            "${String.format("%.1f", meal.fat)}," +
                            "${String.format("%.1f", meal.carbs)}," +
                            "${timeFormat.format(mealDate.time)}\n"
                )
            }
        }

        file
    }
}
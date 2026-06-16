package com.jeywoods.foodcalorieanalyzer.data.local.dao

import androidx.room.*
import com.jeywoods.foodcalorieanalyzer.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY timestamp DESC")
    fun getMealsByDate(date: Date): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getMealsByDateRange(startDate: Date, endDate: Date): Flow<List<MealEntity>>

    @Insert
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Long)

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealById(id: Long): MealEntity?

    @Query("DELETE FROM meals")
    suspend fun deleteAll()
}
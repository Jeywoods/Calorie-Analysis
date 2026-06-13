package com.jeywoods.foodcalorieanalyzer.data.mapper

import com.jeywoods.foodcalorieanalyzer.data.local.entity.MealEntity
import com.jeywoods.foodcalorieanalyzer.data.ml.Food101Metadata
import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import java.util.Date

object MealMapper {

    fun toDomain(entity: MealEntity): Meal {
        val foodItem = Food101Metadata.getFoodById(entity.foodItemId) ?: FoodItem(
            id = entity.foodItemId,
            englishName = entity.englishName,
            russianName = entity.russianName,
            caloriesPer100g = 0f,
            proteinPer100g = 0f,
            fatPer100g = 0f,
            carbsPer100g = 0f
        )

        return Meal(
            id = entity.id,
            foodItem = foodItem,
            grams = entity.grams,
            calories = entity.calories,
            protein = entity.protein,
            fat = entity.fat,
            carbs = entity.carbs,
            imagePath = entity.imagePath,
            timestamp = entity.timestamp
        )
    }

    fun toEntity(meal: Meal): MealEntity {
        return MealEntity(
            id = meal.id,
            foodItemId = meal.foodItem.id,
            englishName = meal.foodItem.englishName,
            russianName = meal.foodItem.russianName,
            grams = meal.grams,
            calories = meal.calories,
            protein = meal.protein,
            fat = meal.fat,
            carbs = meal.carbs,
            imagePath = meal.imagePath,
            timestamp = meal.timestamp,
            date = Date(meal.timestamp)
        )
    }

    fun toDomainList(entities: List<MealEntity>): List<Meal> = entities.map { toDomain(it) }
}
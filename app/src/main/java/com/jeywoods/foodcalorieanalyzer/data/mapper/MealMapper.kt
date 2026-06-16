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
            russianName = entity.russianName
        )

        return Meal(
            id = entity.id,
            foodItem = foodItem,
            grams = entity.grams,
            calories = entity.calories,
            protein = entity.protein,
            fat = entity.fat,
            saturatedFat = entity.saturatedFat,
            carbs = entity.carbs,
            fiber = entity.fiber,
            sugar = entity.sugar,
            sodium = entity.sodium,
            potassium = entity.potassium,
            cholesterol = entity.cholesterol,
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
            saturatedFat = meal.saturatedFat,
            carbs = meal.carbs,
            fiber = meal.fiber,
            sugar = meal.sugar,
            sodium = meal.sodium,
            potassium = meal.potassium,
            cholesterol = meal.cholesterol,
            imagePath = meal.imagePath,
            timestamp = meal.timestamp,
            date = Date(meal.timestamp)
        )
    }

    fun toDomainList(entities: List<MealEntity>): List<Meal> = entities.map { toDomain(it) }
}
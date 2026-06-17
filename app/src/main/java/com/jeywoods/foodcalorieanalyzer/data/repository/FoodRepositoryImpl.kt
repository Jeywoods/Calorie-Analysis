package com.jeywoods.foodcalorieanalyzer.data.repository

import android.util.Log
import com.jeywoods.foodcalorieanalyzer.data.local.dao.NutritionCacheDao
import com.jeywoods.foodcalorieanalyzer.data.local.entity.NutritionCacheEntity
import com.jeywoods.foodcalorieanalyzer.data.ml.Food101Metadata
import com.jeywoods.foodcalorieanalyzer.data.remote.CalorieNinjasApi
import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val api: CalorieNinjasApi,
    private val nutritionCacheDao: NutritionCacheDao
) : FoodRepository {

    override fun getAllFoods(): List<FoodItem> = Food101Metadata.getAllFoods()
    override fun getFoodById(id: Int): FoodItem? = Food101Metadata.getFoodById(id)
    override fun getFoodByEnglishName(name: String): FoodItem? = Food101Metadata.getFoodByEnglishName(name)
    override fun searchFoods(query: String): List<FoodItem> = Food101Metadata.searchFoods(query)
    override fun getFoodCount(): Int = Food101Metadata.getFoodCount()

    suspend fun searchFoodsOnline(query: String): List<FoodItem> = withContext(Dispatchers.IO) {
        try {
            // Проверяем кэш
            val cached = nutritionCacheDao.get(query)
            if (cached != null) {
                Log.d("FoodRepo", "Cache hit: $query")
                return@withContext listOf(cached.toFoodItem())
            }

            Log.d("FoodRepo", "API call: $query")
            val response = api.getNutrition(query)

            response.items?.map { item ->
                val servingSize = item.servingSizeG?.toFloat() ?: 100f
                val multiplier = 100f / servingSize

                val foodItem = FoodItem(
                    id = -1,
                    englishName = item.name ?: query,
                    russianName = item.name ?: query,
                    caloriesPer100g = (item.calories?.toFloat() ?: 0f) * multiplier,
                    proteinPer100g = (item.proteinG?.toFloat() ?: 0f) * multiplier,
                    fatPer100g = (item.fatTotalG?.toFloat() ?: 0f) * multiplier,
                    saturatedFatPer100g = (item.fatSaturatedG?.toFloat() ?: 0f) * multiplier,
                    carbsPer100g = (item.carbohydratesTotalG?.toFloat() ?: 0f) * multiplier,
                    fiberPer100g = (item.fiberG?.toFloat() ?: 0f) * multiplier,
                    sugarPer100g = (item.sugarG?.toFloat() ?: 0f) * multiplier,
                    sodiumPer100g = (item.sodiumMg?.toFloat() ?: 0f) * multiplier,
                    potassiumPer100g = (item.potassiumMg?.toFloat() ?: 0f) * multiplier,
                    cholesterolPer100g = (item.cholesterolMg?.toFloat() ?: 0f) * multiplier
                )

                // Сохраняем в кэш
                nutritionCacheDao.insert(foodItem.toCacheEntity())
                foodItem
            } ?: emptyList()

        } catch (e: Exception) {
            Log.e("FoodRepo", "API error: ${e.message}", e)
            emptyList()
        }
    }

    private fun NutritionCacheEntity.toFoodItem() = FoodItem(
        id = -1,
        englishName = foodName,
        russianName = foodName,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        saturatedFatPer100g = saturatedFatPer100g,
        carbsPer100g = carbsPer100g,
        fiberPer100g = fiberPer100g,
        sugarPer100g = sugarPer100g,
        sodiumPer100g = sodiumPer100g,
        potassiumPer100g = potassiumPer100g,
        cholesterolPer100g = cholesterolPer100g
    )

    private fun FoodItem.toCacheEntity() = NutritionCacheEntity(
        foodName = englishName,
        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        saturatedFatPer100g = saturatedFatPer100g,
        carbsPer100g = carbsPer100g,
        fiberPer100g = fiberPer100g,
        sugarPer100g = sugarPer100g,
        sodiumPer100g = sodiumPer100g,
        potassiumPer100g = potassiumPer100g,
        cholesterolPer100g = cholesterolPer100g
    )
}
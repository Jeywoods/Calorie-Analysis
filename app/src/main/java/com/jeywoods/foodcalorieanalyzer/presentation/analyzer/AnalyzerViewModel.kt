package com.jeywoods.foodcalorieanalyzer.presentation.analyzer

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeywoods.foodcalorieanalyzer.data.repository.FoodRepositoryImpl
import com.jeywoods.foodcalorieanalyzer.domain.model.FoodItem
import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem
import com.jeywoods.foodcalorieanalyzer.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzerViewModel @Inject constructor(
    private val classifyFoodUseCase: ClassifyFoodUseCase,
    private val getTopPredictionsUseCase: GetTopPredictionsUseCase,
    private val calculateNutritionUseCase: CalculateNutritionUseCase,
    private val addMealUseCase: AddMealUseCase,
    private val searchFoodUseCase: SearchFoodUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val foodRepository: FoodRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalyzerUiState>(AnalyzerUiState.Idle)
    val uiState: StateFlow<AnalyzerUiState> = _uiState.asStateFlow()

    private var currentImagePath: String? = null
    private var allPredictions: List<PredictionItem> = emptyList()

    fun onImageSelected(bitmap: Bitmap) {
        _uiState.value = AnalyzerUiState.Analyzing
        viewModelScope.launch {
            try {
                val imagePath = savePhotoUseCase(bitmap)
                currentImagePath = imagePath
                val classificationResult = classifyFoodUseCase(imagePath)
                val topPredictions = getTopPredictionsUseCase(classificationResult)
                allPredictions = topPredictions

                _uiState.value = AnalyzerUiState.PredictionsReady(
                    imagePath = imagePath,
                    predictions = topPredictions,
                    selectedPrediction = topPredictions.firstOrNull()
                )
            } catch (e: Exception) {
                Log.e("AnalyzerVM", "Error", e)
                _uiState.value = AnalyzerUiState.Error(e.message ?: "Ошибка анализа")
            }
        }
    }

    fun onPredictionSelected(prediction: PredictionItem) {
        val currentState = _uiState.value
        when (currentState) {
            is AnalyzerUiState.PredictionsReady -> _uiState.value = currentState.copy(selectedPrediction = prediction)
            is AnalyzerUiState.NutritionCalculated -> calculateNutrition(prediction.foodItem, currentState.grams)
            else -> {}
        }
    }

    fun onGramsChanged(grams: Float) {
        val currentState = _uiState.value
        when (currentState) {
            is AnalyzerUiState.PredictionsReady -> {
                currentState.selectedPrediction?.let {
                    calculateNutrition(it.foodItem, grams)
                    _uiState.value = AnalyzerUiState.NutritionCalculated(
                        imagePath = currentImagePath ?: "",
                        predictions = allPredictions,
                        selectedPrediction = it,
                        grams = grams,
                        calculatedCalories = 0f,
                        calculatedProtein = 0f,
                        calculatedFat = 0f,
                        calculatedCarbs = 0f
                    )
                }
            }
            is AnalyzerUiState.NutritionCalculated -> calculateNutrition(currentState.selectedPrediction.foodItem, grams)
            else -> {}
        }
    }

    private fun calculateNutrition(foodItem: FoodItem, grams: Float) {
        if (grams <= 0) return
        val nutrition = foodItem.calculateNutrition(grams)
        val currentState = _uiState.value
        if (currentState is AnalyzerUiState.NutritionCalculated) {
            _uiState.value = currentState.copy(
                grams = grams,
                calculatedCalories = nutrition.calories,
                calculatedProtein = nutrition.protein,
                calculatedFat = nutrition.fat,
                calculatedSaturatedFat = nutrition.saturatedFat,
                calculatedCarbs = nutrition.carbs,
                calculatedFiber = nutrition.fiber,
                calculatedSugar = nutrition.sugar,
                calculatedSodium = nutrition.sodium,
                calculatedPotassium = nutrition.potassium,
                calculatedCholesterol = nutrition.cholesterol
            )
        }
    }

    fun goToGramsStep() {
        val currentState = _uiState.value
        if (currentState !is AnalyzerUiState.PredictionsReady) return
        val selected = currentState.selectedPrediction ?: return

        _uiState.value = AnalyzerUiState.Analyzing

        viewModelScope.launch {
            try {
                val searchQuery = selected.foodItem.englishName.replace("_", " ")
                val apiData = foodRepository.searchFoodsOnline(searchQuery)
                val enrichedFood = if (apiData.isNotEmpty()) {
                    val apiFood = apiData.first()
                    selected.foodItem.copy(
                        caloriesPer100g = apiFood.caloriesPer100g,
                        proteinPer100g = apiFood.proteinPer100g,
                        fatPer100g = apiFood.fatPer100g,
                        saturatedFatPer100g = apiFood.saturatedFatPer100g,
                        carbsPer100g = apiFood.carbsPer100g,
                        fiberPer100g = apiFood.fiberPer100g,
                        sugarPer100g = apiFood.sugarPer100g,
                        sodiumPer100g = apiFood.sodiumPer100g,
                        potassiumPer100g = apiFood.potassiumPer100g,
                        cholesterolPer100g = apiFood.cholesterolPer100g
                    )
                } else {
                    selected.foodItem
                }

                val enrichedPrediction = selected.copy(foodItem = enrichedFood)
                val grams = 100f
                val nutrition = enrichedFood.calculateNutrition(grams)

                _uiState.value = AnalyzerUiState.NutritionCalculated(
                    imagePath = currentImagePath ?: "",
                    predictions = currentState.predictions,
                    selectedPrediction = enrichedPrediction,
                    grams = grams,
                    calculatedCalories = nutrition.calories,
                    calculatedProtein = nutrition.protein,
                    calculatedFat = nutrition.fat,
                    calculatedSaturatedFat = nutrition.saturatedFat,
                    calculatedCarbs = nutrition.carbs,
                    calculatedFiber = nutrition.fiber,
                    calculatedSugar = nutrition.sugar,
                    calculatedSodium = nutrition.sodium,
                    calculatedPotassium = nutrition.potassium,
                    calculatedCholesterol = nutrition.cholesterol
                )
            } catch (e: Exception) {
                Log.e("AnalyzerVM", "Error fetching nutrition", e)
                val grams = 100f
                val nutrition = selected.foodItem.calculateNutrition(grams)
                _uiState.value = AnalyzerUiState.NutritionCalculated(
                    imagePath = currentImagePath ?: "",
                    predictions = currentState.predictions,
                    selectedPrediction = selected,
                    grams = grams,
                    calculatedCalories = nutrition.calories,
                    calculatedProtein = nutrition.protein,
                    calculatedFat = nutrition.fat,
                    calculatedSaturatedFat = nutrition.saturatedFat,
                    calculatedCarbs = nutrition.carbs,
                    calculatedFiber = nutrition.fiber,
                    calculatedSugar = nutrition.sugar,
                    calculatedSodium = nutrition.sodium,
                    calculatedPotassium = nutrition.potassium,
                    calculatedCholesterol = nutrition.cholesterol
                )
            }
        }
    }

    fun goBackToSelectFood() {
        val currentState = _uiState.value
        if (currentState is AnalyzerUiState.NutritionCalculated) {
            _uiState.value = AnalyzerUiState.PredictionsReady(
                imagePath = currentImagePath ?: "",
                predictions = allPredictions,
                selectedPrediction = currentState.selectedPrediction
            )
        }
    }

    fun onAddToDiary(): Boolean {
        val currentState = _uiState.value
        if (currentState !is AnalyzerUiState.NutritionCalculated) return false

        viewModelScope.launch {
            try {
                addMealUseCase(
                    foodItem = currentState.selectedPrediction.foodItem,
                    grams = currentState.grams,
                    imagePath = currentImagePath
                )
            } catch (e: Exception) {
                Log.e("AnalyzerVM", "Error adding meal", e)
            }
        }
        resetState()
        return true
    }

    fun onSearchFood(query: String): List<FoodItem> = searchFoodUseCase(query)

    fun onManualFoodSelected(foodItem: FoodItem) {
        val predictions = listOf(PredictionItem(foodItem = foodItem, confidence = 1.0f, rank = 1))
        allPredictions = predictions
        _uiState.value = AnalyzerUiState.PredictionsReady(
            imagePath = currentImagePath ?: "",
            predictions = predictions,
            selectedPrediction = predictions.first()
        )
    }

    fun resetState() {
        _uiState.value = AnalyzerUiState.Idle
        currentImagePath = null
        allPredictions = emptyList()
    }

    fun clearError() {
        _uiState.value = AnalyzerUiState.Idle
    }
}
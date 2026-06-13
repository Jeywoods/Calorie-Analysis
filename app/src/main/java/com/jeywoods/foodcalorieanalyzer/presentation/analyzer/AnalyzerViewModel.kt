package com.jeywoods.foodcalorieanalyzer.presentation.analyzer

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val savePhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalyzerUiState>(AnalyzerUiState.Idle)
    val uiState: StateFlow<AnalyzerUiState> = _uiState.asStateFlow()

    private var currentImagePath: String? = null
    private var allPredictions: List<PredictionItem> = emptyList()

    fun onImageSelected(bitmap: Bitmap) {
        Log.d("AnalyzerVM", "=== onImageSelected CALLED ===")
        Log.d("AnalyzerVM", "Bitmap: ${bitmap.width}x${bitmap.height}, config: ${bitmap.config}")

        _uiState.value = AnalyzerUiState.Analyzing

        viewModelScope.launch {
            try {
                Log.d("AnalyzerVM", "Saving photo...")
                val imagePath = savePhotoUseCase(bitmap)
                currentImagePath = imagePath
                Log.d("AnalyzerVM", "Photo saved: $imagePath")

                Log.d("AnalyzerVM", "Starting classification...")
                val classificationResult = classifyFoodUseCase(imagePath)
                Log.d("AnalyzerVM", "Classification done, predictions: ${classificationResult.predictions.size}")

                val topPredictions = getTopPredictionsUseCase(classificationResult)
                allPredictions = topPredictions

                Log.d("AnalyzerVM", "Top predictions: ${topPredictions.size}")
                topPredictions.forEach { pred ->
                    Log.d("AnalyzerVM", "  ${pred.foodItem.russianName}: ${pred.confidencePercent}")
                }

                _uiState.value = AnalyzerUiState.PredictionsReady(
                    imagePath = imagePath,
                    predictions = topPredictions,
                    selectedPrediction = topPredictions.firstOrNull()
                )
                Log.d("AnalyzerVM", "State: PredictionsReady")

            } catch (e: Exception) {
                Log.e("AnalyzerVM", "Error in classification", e)
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
            is AnalyzerUiState.PredictionsReady -> currentState.selectedPrediction?.let { calculateNutrition(it.foodItem, grams) }
            is AnalyzerUiState.NutritionCalculated -> calculateNutrition(currentState.selectedPrediction.foodItem, grams)
            else -> {}
        }
    }

    private fun calculateNutrition(foodItem: FoodItem, grams: Float) {
        if (grams <= 0) return
        val nutrition = calculateNutritionUseCase(foodItem, grams)
        _uiState.value = AnalyzerUiState.NutritionCalculated(
            imagePath = currentImagePath ?: "",
            predictions = allPredictions,
            selectedPrediction = allPredictions.find { it.foodItem.id == foodItem.id } ?: allPredictions.first(),
            grams = grams,
            calculatedCalories = nutrition.calories,
            calculatedProtein = nutrition.protein,
            calculatedFat = nutrition.fat,
            calculatedCarbs = nutrition.carbs
        )
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
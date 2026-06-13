package com.jeywoods.foodcalorieanalyzer.presentation.analyzer

import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem

sealed interface AnalyzerUiState {
    object Idle : AnalyzerUiState
    object Analyzing : AnalyzerUiState

    data class PredictionsReady(
        val imagePath: String,
        val predictions: List<PredictionItem>,
        val selectedPrediction: PredictionItem?
    ) : AnalyzerUiState

    data class NutritionCalculated(
        val imagePath: String,
        val predictions: List<PredictionItem>,
        val selectedPrediction: PredictionItem,
        val grams: Float,
        val calculatedCalories: Float,
        val calculatedProtein: Float,
        val calculatedFat: Float,
        val calculatedCarbs: Float
    ) : AnalyzerUiState

    data class MealAdded(val mealId: Long) : AnalyzerUiState
    data class Error(val message: String) : AnalyzerUiState
}
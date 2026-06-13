package com.jeywoods.foodcalorieanalyzer.domain.model

data class ClassificationResult(
    val predictions: List<Prediction>,
    val processingTimeMs: Long
) {
    data class Prediction(
        val foodItem: FoodItem,
        val confidence: Float,
        val index: Int
    )
}
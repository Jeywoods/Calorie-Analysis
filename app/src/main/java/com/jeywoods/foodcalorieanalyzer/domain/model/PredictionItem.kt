package com.jeywoods.foodcalorieanalyzer.domain.model

data class PredictionItem(
    val foodItem: FoodItem,
    val confidence: Float,
    val rank: Int
) {
    val confidencePercent: String
        get() = "${(confidence * 100).toInt()}%"

    val isHighConfidence: Boolean
        get() = confidence >= 0.7f
}
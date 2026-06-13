package com.jeywoods.foodcalorieanalyzer.domain.usecase

import com.jeywoods.foodcalorieanalyzer.domain.model.ClassificationResult
import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem
import javax.inject.Inject

class GetTopPredictionsUseCase @Inject constructor() {

    operator fun invoke(classificationResult: ClassificationResult): List<PredictionItem> {
        return classificationResult.predictions
            .take(3)
            .mapIndexed { index, prediction ->
                PredictionItem(
                    foodItem = prediction.foodItem,
                    confidence = prediction.confidence,
                    rank = index + 1
                )
            }
    }
}
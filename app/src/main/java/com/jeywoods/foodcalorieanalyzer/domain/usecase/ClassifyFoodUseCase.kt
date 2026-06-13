package com.jeywoods.foodcalorieanalyzer.domain.usecase

import android.util.Log
import com.jeywoods.foodcalorieanalyzer.domain.model.ClassificationResult
import com.jeywoods.foodcalorieanalyzer.domain.repository.PhotoRepository
import javax.inject.Inject

class ClassifyFoodUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(imagePath: String): ClassificationResult {
        Log.d("ClassifyUC", "=== ClassifyFoodUseCase ===")
        Log.d("ClassifyUC", "Image: $imagePath")

        // ВРЕМЕННО ОТКЛЮЧАЕМ КЭШ
        val imageHash = photoRepository.calculateImageHash(imagePath)
        Log.d("ClassifyUC", "Hash: $imageHash")

        val cachedResult = photoRepository.getCachedClassification(imageHash)
        Log.d("ClassifyUC", "Cached: ${cachedResult != null}")

        if (cachedResult != null) {
            Log.d("ClassifyUC", "!!! RETURNING CACHED - SKIPPING CLASSIFICATION !!!")
            // Закомментируйте return чтобы всегда делать новую классификацию:
            // return cachedResult
        }

        Log.d("ClassifyUC", "Running classification...")
        val result = photoRepository.classifyFood(imagePath)
        Log.d("ClassifyUC", "Classification done")

        photoRepository.cacheClassification(imageHash, result)

        return result
    }
}
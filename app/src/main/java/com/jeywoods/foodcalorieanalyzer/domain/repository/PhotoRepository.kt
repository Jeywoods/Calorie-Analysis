package com.jeywoods.foodcalorieanalyzer.domain.repository


import android.graphics.Bitmap
import com.jeywoods.foodcalorieanalyzer.domain.model.ClassificationResult

interface PhotoRepository {
    suspend fun savePhoto(bitmap: Bitmap): String
    suspend fun classifyFood(imagePath: String): ClassificationResult
    suspend fun getCachedClassification(imageHash: String): ClassificationResult?
    suspend fun cacheClassification(imageHash: String, result: ClassificationResult)
    suspend fun calculateImageHash(imagePath: String): String
    suspend fun loadBitmap(imagePath: String, maxWidth: Int, maxHeight: Int): Bitmap?
}
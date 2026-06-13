package com.jeywoods.foodcalorieanalyzer.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jeywoods.foodcalorieanalyzer.data.local.dao.ClassificationCacheDao
import com.jeywoods.foodcalorieanalyzer.data.ml.ImageHasher
import com.jeywoods.foodcalorieanalyzer.data.ml.TFLiteFoodClassifier
import com.jeywoods.foodcalorieanalyzer.domain.model.ClassificationResult
import com.jeywoods.foodcalorieanalyzer.domain.repository.PhotoRepository
import com.jeywoods.foodcalorieanalyzer.data.local.entity.ClassificationCacheEntity
import com.jeywoods.foodcalorieanalyzer.data.ml.Food101Metadata
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val classifier: TFLiteFoodClassifier,
    private val cacheDao: ClassificationCacheDao
) : PhotoRepository {

    override suspend fun savePhoto(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        val mealsDir = File(context.filesDir, "meals")
        if (!mealsDir.exists()) mealsDir.mkdirs()

        val fileName = "meal_${UUID.randomUUID()}.jpg"
        val file = File(mealsDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        file.absolutePath
    }

    override suspend fun classifyFood(imagePath: String): ClassificationResult {
        val bitmap = loadBitmap(imagePath, 224, 224)
            ?: throw IllegalStateException("Cannot load image")
        return classifier.classify(bitmap)
    }

    override suspend fun getCachedClassification(imageHash: String): ClassificationResult? {
        val cacheEntity = cacheDao.getCacheByHash(imageHash) ?: return null

        return try {
            val jsonArray = JSONArray(cacheEntity.predictionsJson)
            val predictions = mutableListOf<ClassificationResult.Prediction>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val foodItemId = jsonObject.getInt("foodItemId")
                val confidence = jsonObject.getDouble("confidence").toFloat()
                val index = jsonObject.getInt("index")

                val foodItem = Food101Metadata.getFoodById(foodItemId)
                    ?: throw IllegalStateException("Unknown food ID: $foodItemId")

                predictions.add(
                    ClassificationResult.Prediction(
                        foodItem = foodItem,
                        confidence = confidence,
                        index = index
                    )
                )
            }

            ClassificationResult(
                predictions = predictions,
                processingTimeMs = 0
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun cacheClassification(imageHash: String, result: ClassificationResult) {
        val jsonArray = JSONArray()

        result.predictions.forEach { prediction ->
            val jsonObject = JSONObject()
            jsonObject.put("foodItemId", prediction.foodItem.id)
            jsonObject.put("confidence", prediction.confidence.toDouble())
            jsonObject.put("index", prediction.index)
            jsonArray.put(jsonObject)
        }

        val predictionsJson = jsonArray.toString()

        val cacheEntity = ClassificationCacheEntity(
            imageHash = imageHash,
            topPredictionId = result.predictions.firstOrNull()?.foodItem?.id ?: 0,
            topPredictionConfidence = result.predictions.firstOrNull()?.confidence ?: 0f,
            predictionsJson = predictionsJson
        )

        cacheDao.insertCache(cacheEntity)
    }

    override suspend fun calculateImageHash(imagePath: String): String = withContext(Dispatchers.IO) {
        val bitmap = loadBitmap(imagePath, 224, 224)
            ?: throw IllegalStateException("Cannot load image for hashing")
        ImageHasher.calculateDHash(bitmap)
    }

    override suspend fun loadBitmap(imagePath: String, maxWidth: Int, maxHeight: Int): Bitmap? {
        return withContext(Dispatchers.IO) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imagePath, options)

            options.inSampleSize = calculateInSampleSize(
                options.outWidth, options.outHeight, maxWidth, maxHeight
            )
            options.inJustDecodeBounds = false

            BitmapFactory.decodeFile(imagePath, options)
        }
    }

    private fun calculateInSampleSize(
        rawWidth: Int, rawHeight: Int,
        reqWidth: Int, reqHeight: Int
    ): Int {
        var inSampleSize = 1
        if (rawHeight > reqHeight || rawWidth > reqWidth) {
            val halfHeight = rawHeight / 2
            val halfWidth = rawWidth / 2
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
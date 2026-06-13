package com.jeywoods.foodcalorieanalyzer.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.jeywoods.foodcalorieanalyzer.domain.model.ClassificationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp

@Singleton
class TFLiteFoodClassifier @Inject constructor(
    private val context: Context
) {
    private var interpreter: Interpreter? = null
    private val numClasses = 101

    // Параметры EfficientNet-B3
    private val inputSize = 300

    // ImageNet нормализация
    private val meanR = 0.485f
    private val meanG = 0.456f
    private val meanB = 0.406f
    private val stdR = 0.229f
    private val stdG = 0.224f
    private val stdB = 0.225f

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val modelBuffer: MappedByteBuffer = FileUtil.loadMappedFile(context, "model.tflite")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
            Log.d("TFLite", "Model loaded successfully")
        } catch (e: Exception) {
            Log.e("TFLite", "Failed to load model", e)
            throw RuntimeException("Failed to load TFLite model", e)
        }
    }

    suspend fun classify(bitmap: Bitmap): ClassificationResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        // Предобработка: CenterCrop + Resize до 300x300
        val processed = preprocessImage(bitmap)

        // Создаем входной буфер (300*300*3*4 = 1,080,000 байт)
        val bufferSize = inputSize * inputSize * 3 * 4
        val inputBuffer = ByteBuffer.allocateDirect(bufferSize).apply {
            order(ByteOrder.nativeOrder())
        }

        val pixels = IntArray(inputSize * inputSize)
        processed.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        // ImageNet нормализация
        for (pixel in pixels) {
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            inputBuffer.putFloat((r - meanR) / stdR)
            inputBuffer.putFloat((g - meanG) / stdG)
            inputBuffer.putFloat((b - meanB) / stdB)
        }

        // Запуск модели
        val outputBuffer = Array(1) { FloatArray(numClasses) }
        interpreter?.run(inputBuffer, outputBuffer)

        // Softmax
        val logits = outputBuffer[0]
        val maxLogit = logits.max()
        val expSum = logits.sumOf { exp((it - maxLogit).toDouble()) }
        val probabilities = logits.map { (exp((it - maxLogit).toDouble()) / expSum).toFloat() }

        // Топ-5
        val predictions = probabilities
            .mapIndexed { index, prob ->
                ClassificationResult.Prediction(
                    foodItem = Food101Metadata.getFoodById(index)
                        ?: throw IllegalStateException("Unknown food index: $index"),
                    confidence = prob,
                    index = index
                )
            }
            .sortedByDescending { it.confidence }
            .take(5)

        ClassificationResult(
            predictions = predictions,
            processingTimeMs = System.currentTimeMillis() - startTime
        )
    }

    // CenterCrop до квадрата, затем Resize до 300x300
    private fun preprocessImage(bitmap: Bitmap): Bitmap {
        val minSide = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - minSide) / 2
        val y = (bitmap.height - minSide) / 2
        val cropped = Bitmap.createBitmap(bitmap, x.coerceAtLeast(0), y.coerceAtLeast(0), minSide, minSide)
        return Bitmap.createScaledBitmap(cropped, inputSize, inputSize, true)
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
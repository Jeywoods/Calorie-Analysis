package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt

@Composable
fun ImageCropperDialog(
    bitmap: Bitmap,
    onCropComplete: (Bitmap) -> Unit,
    onDismiss: () -> Unit
) {
    var cropCenterX by remember { mutableFloatStateOf(0f) }
    var cropCenterY by remember { mutableFloatStateOf(0f) }
    var canvasSize by remember { mutableStateOf(IntSize(0, 0)) }

    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }

    val cropDisplaySize = remember(canvasSize) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            minOf(canvasSize.width, canvasSize.height) * 0.8f
        } else 0f
    }

    LaunchedEffect(canvasSize) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            cropCenterX = canvasSize.width / 2f
            cropCenterY = canvasSize.height / 2f
            Log.d("Cropper", "Canvas size: ${canvasSize.width}x${canvasSize.height}, crop: $cropDisplaySize")
        }
    }

    Log.d("Cropper", "ImageCropperDialog created, bitmap: ${bitmap.width}x${bitmap.height}")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.Black).padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        Log.d("Cropper", "Dialog dismissed")
                        onDismiss()
                    }) {
                        Icon(Icons.Default.Close, "Отмена", tint = Color.White)
                    }
                    Text("Обрежьте изображение", color = Color.White)
                    IconButton(onClick = {
                        Log.d("Cropper", "=== CONFIRM CROP ===")

                        if (cropDisplaySize > 0 && canvasSize.width > 0) {
                            // Соотношение сторон
                            val bitmapRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                            val canvasRatio = canvasSize.width.toFloat() / canvasSize.height.toFloat()

                            // Вычисляем как изображение вписывается в Canvas (ContentScale.Fit)
                            val displayWidth: Float
                            val displayHeight: Float
                            val offsetX: Float
                            val offsetY: Float

                            if (bitmapRatio > canvasRatio) {
                                // Изображение шире — помещается по ширине
                                displayWidth = canvasSize.width.toFloat()
                                displayHeight = canvasSize.width.toFloat() / bitmapRatio
                                offsetX = 0f
                                offsetY = (canvasSize.height - displayHeight) / 2f
                            } else {
                                // Изображение выше — помещается по высоте
                                displayHeight = canvasSize.height.toFloat()
                                displayWidth = canvasSize.height.toFloat() * bitmapRatio
                                offsetX = (canvasSize.width - displayWidth) / 2f
                                offsetY = 0f
                            }

                            // Переводим координаты кропа в координаты оригинального изображения
                            val scaleX = bitmap.width.toFloat() / displayWidth
                            val scaleY = bitmap.height.toFloat() / displayHeight

                            val cropLeftOnBitmap = ((cropCenterX - cropDisplaySize / 2 - offsetX) * scaleX).roundToInt()
                            val cropTopOnBitmap = ((cropCenterY - cropDisplaySize / 2 - offsetY) * scaleY).roundToInt()
                            val cropSizeOnBitmap = (cropDisplaySize * scaleX).roundToInt()

                            // Обрезаем с проверкой границ
                            val safeLeft = cropLeftOnBitmap.coerceIn(0, bitmap.width - 1)
                            val safeTop = cropTopOnBitmap.coerceIn(0, bitmap.height - 1)
                            val safeSize = cropSizeOnBitmap.coerceIn(1, minOf(bitmap.width - safeLeft, bitmap.height - safeTop))

                            val cropped = Bitmap.createBitmap(bitmap, safeLeft, safeTop, safeSize, safeSize)
                            val resized = Bitmap.createScaledBitmap(cropped, 300, 300, true)
                            val rgbBitmap = resized.copy(Bitmap.Config.ARGB_8888, false)

                            onCropComplete(rgbBitmap)
                        }
                    }) {
                        Icon(Icons.Default.Check, "Готово", tint = Color(0xFF4CAF50))
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                        .onSizeChanged {
                            canvasSize = it
                            Log.d("Cropper", "onSizeChanged: ${it.width}x${it.height}")
                        }
                        .pointerInput(canvasSize, cropDisplaySize) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                val bitmapRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                                val canvasRatio = canvasSize.width.toFloat() / canvasSize.height.toFloat()

                                val imageWidth: Float
                                val imageHeight: Float
                                val imageLeft: Float
                                val imageTop: Float

                                if (bitmapRatio > canvasRatio) {
                                    imageWidth = canvasSize.width.toFloat()
                                    imageHeight = canvasSize.width.toFloat() / bitmapRatio
                                    imageLeft = 0f
                                    imageTop = (canvasSize.height - imageHeight) / 2f
                                } else {
                                    imageHeight = canvasSize.height.toFloat()
                                    imageWidth = canvasSize.height.toFloat() * bitmapRatio
                                    imageLeft = (canvasSize.width - imageWidth) / 2f
                                    imageTop = 0f
                                }

                                val imageRight = imageLeft + imageWidth
                                val imageBottom = imageTop + imageHeight

                                // Квадрат не может быть больше изображения
                                val halfSize = minOf(cropDisplaySize / 2, imageWidth / 2, imageHeight / 2)

                                val minX = imageLeft + halfSize
                                val maxX = imageRight - halfSize
                                val minY = imageTop + halfSize
                                val maxY = imageBottom - halfSize

                                if (minX <= maxX) {
                                    cropCenterX = (cropCenterX + dragAmount.x).coerceIn(minX, maxX)
                                }
                                if (minY <= maxY) {
                                    cropCenterY = (cropCenterY + dragAmount.y).coerceIn(minY, maxY)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(bitmap = imageBitmap, contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit)

                    if (cropDisplaySize > 0) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val halfSize = cropDisplaySize / 2
                            val left = cropCenterX - halfSize
                            val top = cropCenterY - halfSize

                            drawRect(Color.Black.copy(alpha = 0.6f), Offset(0f, 0f),
                                androidx.compose.ui.geometry.Size(size.width, top))
                            drawRect(Color.Black.copy(alpha = 0.6f),
                                Offset(0f, top + cropDisplaySize),
                                androidx.compose.ui.geometry.Size(size.width, size.height - top - cropDisplaySize))
                            drawRect(Color.Black.copy(alpha = 0.6f), Offset(0f, top),
                                androidx.compose.ui.geometry.Size(left, cropDisplaySize))
                            drawRect(Color.Black.copy(alpha = 0.6f),
                                Offset(left + cropDisplaySize, top),
                                androidx.compose.ui.geometry.Size(size.width - left - cropDisplaySize, cropDisplaySize))

                            drawRect(Color.White, Offset(left, top),
                                androidx.compose.ui.geometry.Size(cropDisplaySize, cropDisplaySize),
                                style = Stroke(width = 2f))
                        }
                    }
                }

                Text("Двигайте квадрат. Изображение будет обрезано до 224×224",
                    modifier = Modifier.fillMaxWidth().background(Color.Black).padding(16.dp),
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
}
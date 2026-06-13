package com.jeywoods.foodcalorieanalyzer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.ui.unit.IntSize
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

object ImageUtils {

    fun getBitmapFromUri(context: Context, uri: Uri, maxSize: Int = 1024): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bytes = inputStream.use { it.readBytes() }
            decodeSampledBitmap(bytes, maxSize, maxSize)
        } catch (e: Exception) {
            null
        }
    }

    fun decodeSampledBitmap(data: ByteArray, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeByteArray(data, 0, data.size, options)

        options.inSampleSize = calculateInSampleSize(
            options.outWidth, options.outHeight, reqWidth, reqHeight
        )
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeByteArray(data, 0, data.size, options)!!
    }

    private fun calculateInSampleSize(
        width: Int, height: Int, reqWidth: Int, reqHeight: Int
    ): Int {
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun rotateImageIfNeeded(context: Context, photoUri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(photoUri) ?: return null
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            inputStream.close()

            val bitmap = getBitmapFromUri(context, photoUri) ?: return null

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            if (rotation == 0f) return bitmap

            val matrix = Matrix().apply { postRotate(rotation) }
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            if (rotatedBitmap != bitmap) {
                bitmap.recycle()
            }

            rotatedBitmap
        } catch (e: Exception) {
            null
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, directory: String = "meals"): File {
        val dir = File(context.filesDir, directory)
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "meal_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        return file
    }

    fun bitmapToByteArray(bitmap: Bitmap, quality: Int = 90): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    fun cropBitmap(
        bitmap: Bitmap,
        cropSize: IntSize,
        offsetX: Float,
        offsetY: Float,
        scale: Float,
        canvasSize: IntSize
    ): Bitmap {
        val scaleX = bitmap.width.toFloat() / canvasSize.width
        val scaleY = bitmap.height.toFloat() / canvasSize.height
        val baseScale = minOf(scaleX, scaleY)

        val cropWidth = (cropSize.width * scale * baseScale).roundToInt()
        val cropHeight = (cropSize.height * scale * baseScale).roundToInt()

        val cropX = ((canvasSize.width - cropSize.width * scale) / 2 + offsetX).roundToInt()
            .coerceIn(0, bitmap.width - cropWidth)
        val cropY = ((canvasSize.height - cropSize.height * scale) / 2 + offsetY).roundToInt()
            .coerceIn(0, bitmap.height - cropHeight)

        return Bitmap.createBitmap(bitmap, cropX, cropY, cropWidth, cropHeight)
    }
}
package com.jeywoods.foodcalorieanalyzer.data.ml

import android.graphics.Bitmap

object ImageHasher {
    private const val HASH_SIZE = 16
    private const val SIMILARITY_THRESHOLD = 0.9

    fun calculateDHash(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, HASH_SIZE + 1, HASH_SIZE, true)
        val pixels = IntArray((HASH_SIZE + 1) * HASH_SIZE)
        resized.getPixels(pixels, 0, HASH_SIZE + 1, 0, 0, HASH_SIZE + 1, HASH_SIZE)

        val hash = StringBuilder()
        for (row in 0 until HASH_SIZE) {
            for (col in 0 until HASH_SIZE) {
                val leftPixel = pixels[row * (HASH_SIZE + 1) + col] and 0xFF
                val rightPixel = pixels[row * (HASH_SIZE + 1) + col + 1] and 0xFF
                hash.append(if (leftPixel < rightPixel) "1" else "0")
            }
        }

        return hash.toString()
    }

    fun calculateSimilarity(hash1: String, hash2: String): Float {
        if (hash1.length != hash2.length) return 0f

        var matches = 0
        for (i in hash1.indices) {
            if (hash1[i] == hash2[i]) matches++
        }

        return matches.toFloat() / hash1.length
    }

    fun areSimilar(hash1: String, hash2: String): Boolean {
        return calculateSimilarity(hash1, hash2) >= SIMILARITY_THRESHOLD
    }
}
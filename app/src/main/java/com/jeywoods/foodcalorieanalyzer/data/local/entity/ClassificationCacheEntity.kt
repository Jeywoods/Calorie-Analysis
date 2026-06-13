package com.jeywoods.foodcalorieanalyzer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification_cache")
data class ClassificationCacheEntity(
    @PrimaryKey
    val imageHash: String,
    val topPredictionId: Int,
    val topPredictionConfidence: Float,
    val predictionsJson: String, // JSON array of all top-5 predictions
    val timestamp: Long = System.currentTimeMillis()
)
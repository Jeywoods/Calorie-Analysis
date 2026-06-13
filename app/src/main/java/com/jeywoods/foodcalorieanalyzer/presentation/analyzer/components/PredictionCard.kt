package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem
import com.jeywoods.foodcalorieanalyzer.ui.theme.ConfirmedGreen
import com.jeywoods.foodcalorieanalyzer.ui.theme.*

@Composable
fun PredictionCard(
    prediction: PredictionItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prediction.foodItem.russianName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = prediction.foodItem.englishName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onClick
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confidence bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = prediction.confidencePercent,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (prediction.isHighConfidence) ConfirmedGreen else Warning
                )

                Spacer(modifier = Modifier.width(8.dp))

                LinearProgressIndicator(
                    progress = { prediction.confidence },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = if (prediction.isHighConfidence) ConfirmedGreen else Warning,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mini nutrition table
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MiniNutritionItem("Ккал", "${prediction.foodItem.caloriesPer100g.toInt()}", CaloriesColor)
                MiniNutritionItem("Б", "${String.format("%.1f", prediction.foodItem.proteinPer100g)}г", ProteinColor)
                MiniNutritionItem("Ж", "${String.format("%.1f", prediction.foodItem.fatPer100g)}г", FatColor)
                MiniNutritionItem("У", "${String.format("%.1f", prediction.foodItem.carbsPer100g)}г", CarbsColor)
            }

            Text(
                text = "на 100г",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun MiniNutritionItem(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
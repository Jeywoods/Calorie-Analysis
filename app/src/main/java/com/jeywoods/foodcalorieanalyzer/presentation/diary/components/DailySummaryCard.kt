package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.AnalyzerUiState
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import java.util.Locale

@Composable
fun NutritionContent(
    state: AnalyzerUiState.NutritionCalculated,
    onGramsChanged: (Float) -> Unit,
    onAddToDiary: () -> Unit,
    onBackToSelect: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // TOP BAR
        Surface(
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackToSelect) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Шаг 2 из 2",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Порция и питательность",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(onClick = onCancel) {
                    Text("Отмена", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Food Title
            Text(
                state.selectedPrediction.foodItem.russianName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Image Card
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box {
                    AsyncImage(
                        model = state.imagePath,
                        contentDescription = state.selectedPrediction.foodItem.russianName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }
            }

            // Gram Input
            GramInputSection(
                onGramsChanged = onGramsChanged,
                initialGrams = state.grams
            )

            // Add to Diary Button
            Button(
                onClick = onAddToDiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Добавить в дневник",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Main Nutrition Card
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    // Calories & Portion
                    CaloriesDisplay(
                        calories = state.calculatedCalories.toInt(),
                        grams = state.grams.toInt()
                    )

                    // Macronutrients
                    Text(
                        "Макронутриенты",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EnhancedMacroCard(
                            modifier = Modifier.weight(1f),
                            title = "Белки",
                            value = state.calculatedProtein,
                            color = ProteinColor,
                            icon = Icons.Default.FitnessCenter
                        )
                        EnhancedMacroCard(
                            modifier = Modifier.weight(1f),
                            title = "Жиры",
                            value = state.calculatedFat,
                            color = FatColor,
                            icon = Icons.Default.WaterDrop
                        )
                        EnhancedMacroCard(
                            modifier = Modifier.weight(1f),
                            title = "Углеводы",
                            value = state.calculatedCarbs,
                            color = CarbsColor,
                            icon = Icons.Default.Grain
                        )
                    }

                    // Divider
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    // Detailed Nutrients
                    Text(
                        "Подробный состав",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailedNutrientCard(
                            label = "Насыщенные жиры",
                            value = state.calculatedSaturatedFat,
                            unit = "г",
                            color = FatColor,
                            icon = Icons.Default.LocalFireDepartment
                        )
                        DetailedNutrientCard(
                            label = "Клетчатка",
                            value = state.calculatedFiber,
                            unit = "г",
                            color = Color(0xFF4CAF50),
                            icon = Icons.Default.Spa
                        )
                        DetailedNutrientCard(
                            label = "Сахар",
                            value = state.calculatedSugar,
                            unit = "г",
                            color = Color(0xFFFF9800),
                            icon = Icons.Default.Icecream
                        )
                        DetailedNutrientCard(
                            label = "Натрий",
                            value = state.calculatedSodium,
                            unit = "мг",
                            color = Color(0xFF2196F3),
                            icon = Icons.Default.Science
                        )
                        DetailedNutrientCard(
                            label = "Калий",
                            value = state.calculatedPotassium,
                            unit = "мг",
                            color = Color(0xFF9C27B0),
                            icon = Icons.Default.Bolt
                        )
                        DetailedNutrientCard(
                            label = "Холестерин",
                            value = state.calculatedCholesterol,
                            unit = "мг",
                            color = Color(0xFF607D8B),
                            icon = Icons.Default.FavoriteBorder
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CaloriesDisplay(
    calories: Int,
    grams: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Primary.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "$calories",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary
                )
                Text(
                    "ккал",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Primary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Порция: $grams г",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EnhancedMacroCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Float,
    color: Color,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icon in circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    String.format(Locale.US, "%.1f", value),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "г",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DetailedNutrientCard(
    label: String,
    value: Float,
    unit: String,
    color: Color,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Icon with colored circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            // Value pill
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.1f)
            ) {
                Text(
                    "${String.format(Locale.US, "%.1f", value)} $unit",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}
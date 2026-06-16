package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                        Icons.Default.ArrowBack,
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
                        "Настройка блюда",
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

            // FOOD NAME
            Text(
                state.selectedPrediction.foodItem.russianName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // КАРТИНКА БЛЮДА
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AsyncImage(
                    model = state.imagePath,
                    contentDescription = state.selectedPrediction.foodItem.russianName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // ВВОД ГРАММОВ
            GramInputSection(
                onGramsChanged = onGramsChanged,
                initialGrams = state.grams
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    // КАЛОРИИ
                    Text(
                        "${state.calculatedCalories.toInt()} ккал",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    // СЕТКА 3×3
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Первый ряд
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Белки",
                                value = state.calculatedProtein,
                                unit = "г"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Жиры",
                                value = state.calculatedFat,
                                unit = "г"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Углеводы",
                                value = state.calculatedCarbs,
                                unit = "г"
                            )
                        }

                        // Второй ряд
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Насыщ. жиры",
                                value = state.calculatedSaturatedFat,
                                unit = "г"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Клетчатка",
                                value = state.calculatedFiber,
                                unit = "г"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Сахар",
                                value = state.calculatedSugar,
                                unit = "г"
                            )
                        }

                        // Третий ряд
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Натрий",
                                value = state.calculatedSodium,
                                unit = "мг"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Калий",
                                value = state.calculatedPotassium,
                                unit = "мг"
                            )
                            NutrientTile(
                                modifier = Modifier.weight(1f),
                                label = "Холестерин",
                                value = state.calculatedCholesterol,
                                unit = "мг"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // КНОПКА ВСЕГДА ВИДНА
        Surface(
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Button(
                onClick = onAddToDiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(52.dp),
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
        }
    }
}

@Composable
private fun NutrientTile(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    unit: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    String.format(Locale.US, "%.1f", value),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    unit,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
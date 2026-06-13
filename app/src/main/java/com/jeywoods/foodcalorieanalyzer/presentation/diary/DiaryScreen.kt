package com.jeywoods.foodcalorieanalyzer.presentation.diary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.MealCard
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.Screen
import java.util.Locale

@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.mealToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelDelete() },
            title = { Text("Удалить приём пищи?") },
            text = { Text("Вы уверены, что хотите удалить ${uiState.mealToDelete?.foodItem?.russianName}?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDelete() }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDelete() }) { Text("Отмена") }
            }
        )
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.meals.isEmpty() -> {
            EmptyDiaryContent(onNavigateToAnalyzer = { navController.navigate(Screen.Analyzer.route) })
        }

        else -> {
            DiaryContent(
                uiState = uiState,
                onGramsChanged = { mealId, grams -> viewModel.onGramsChanged(mealId, grams) },
                onDeleteMeal = { meal -> viewModel.onDeleteMeal(meal) }
            )
        }
    }
}

@Composable
private fun EmptyDiaryContent(onNavigateToAnalyzer: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Book, null, Modifier.size(120.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Сегодня ещё нет записей", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Добавьте приём пищи через Анализ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToAnalyzer) {
            Icon(Icons.Default.CameraAlt, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Перейти к анализу")
        }
    }
}

@Composable
private fun DiaryContent(
    uiState: DiaryUiState,
    onGramsChanged: (Long, Float) -> Unit,
    onDeleteMeal: (Meal) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        uiState.dailySummary?.let { summary -> DailySummaryCard(summary = summary) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = uiState.meals, key = { it.id }) { meal ->
                MealCard(
                    meal = meal,
                    onGramsChanged = { newGrams -> onGramsChanged(meal.id, newGrams) },
                    onDelete = { onDeleteMeal(meal) }
                )
            }
        }
    }
}

@Composable
private fun DailySummaryCard(summary: DailySummary) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Итого за день", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryItem("Калории", "${summary.totalCalories.toInt()}", "ккал", Color.White)
                SummaryItem("Белки", String.format(Locale.US, "%.1f", summary.totalProtein), "г", Color.White)
                SummaryItem("Жиры", String.format(Locale.US, "%.1f", summary.totalFat), "г", Color.White)
                SummaryItem("Углеводы", String.format(Locale.US, "%.1f", summary.totalCarbs), "г", Color.White)
            }
            if (summary.mealCount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Приёмов пищи: ${summary.mealCount}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String, unit: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(unit, style = MaterialTheme.typography.bodySmall, color = color.copy(alpha = 0.8f))
        Text(label, style = MaterialTheme.typography.bodySmall, color = color.copy(alpha = 0.7f))
    }
}
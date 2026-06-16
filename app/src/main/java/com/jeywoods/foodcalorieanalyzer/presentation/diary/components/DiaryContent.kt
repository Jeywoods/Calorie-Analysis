package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.presentation.diary.DiaryUiState

@Composable
fun DiaryContent(
    uiState: DiaryUiState,
    onGramsChanged: (Long, Float) -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    onNavigateToAnalyzer: () -> Unit
) {
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Итоговая сводка за день
        uiState.dailySummary?.let { summary ->
            DailySummaryCard(summary = summary)
        }

        // Список приёмов пищи
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Приёмы пищи",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            itemsIndexed(
                items = uiState.meals,
                key = { _, meal -> meal.id }
            ) { index, meal ->
                MealCard(
                    meal = meal,
                    onGramsChanged = { newGrams ->
                        onGramsChanged(meal.id, newGrams)
                    },
                    onDelete = { onDeleteMeal(meal) },
                    listState = listState,
                    itemIndex = index + 1
                )
            }
        }
    }
}
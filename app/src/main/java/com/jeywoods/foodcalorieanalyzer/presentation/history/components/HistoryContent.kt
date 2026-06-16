package com.jeywoods.foodcalorieanalyzer.presentation.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.presentation.history.HistoryUiState
import java.util.*

@Composable
fun HistoryContent(
    uiState: HistoryUiState,
    onDateSelected: (Date) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Календарь
        item {
            CalorieCalendar(
                dailySummaries = uiState.dailySummaries,
                selectedDate = uiState.selectedDate,
                onDateSelected = onDateSelected
            )
        }

        // Заголовок выбранного дня + сводка
        if (uiState.selectedDayMeals.isNotEmpty()) {
            item {
                DaySummaryHeader(
                    meals = uiState.selectedDayMeals,
                    selectedDate = uiState.selectedDate
                )
            }

            items(uiState.selectedDayMeals) { meal ->
                HistoryMealCard(meal = meal)
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Выберите день в календаре\nдля просмотра приёмов пищи",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
            }
        }
    }
}
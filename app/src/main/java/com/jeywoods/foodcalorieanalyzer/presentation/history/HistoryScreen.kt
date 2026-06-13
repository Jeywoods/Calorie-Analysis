package com.jeywoods.foodcalorieanalyzer.presentation.history

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.presentation.history.components.CalorieCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.exportedFile) {
        uiState.exportedFile?.let { file ->
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Поделиться CSV"))
            viewModel.clearExportedFile()
        }
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            HistoryContent(
                uiState = uiState,
                onDateSelected = { date -> viewModel.onDateSelected(date) }
            )
        }
    }
}

@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    onDateSelected: (Date) -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

    Column(modifier = Modifier.fillMaxSize()) {
        CalorieCalendar(
            dailySummaries = uiState.dailySummaries,
            selectedDate = null,
            onDateSelected = onDateSelected
        )

        HorizontalDivider()

        if (uiState.selectedDayMeals.isNotEmpty()) {
            Text(
                text = "Приёмы пищи за выбранный день",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(uiState.selectedDayMeals) { meal ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(meal.foodItem.russianName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                val mealTime = Calendar.getInstance().apply { timeInMillis = meal.timestamp }
                                Text("${meal.grams.toInt()}г • ${timeFormat.format(mealTime.time)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${meal.calories.toInt()} ккал", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text("Б:${String.format(Locale.US, "%.1f", meal.protein)} Ж:${String.format(Locale.US, "%.1f", meal.fat)} У:${String.format(Locale.US, "%.1f", meal.carbs)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Выберите день в календаре\nдля просмотра приёмов пищи", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}
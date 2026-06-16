package com.jeywoods.foodcalorieanalyzer.presentation.diary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.DeleteMealDialog
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.DiaryContent
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.EmptyDiaryContent
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.Screen

@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Диалог подтверждения удаления
    if (uiState.mealToDelete != null) {
        DeleteMealDialog(
            mealName = uiState.mealToDelete?.foodItem?.russianName ?: "",
            onConfirm = { viewModel.confirmDelete() },
            onDismiss = { viewModel.cancelDelete() }
        )
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        uiState.meals.isEmpty() -> {
            EmptyDiaryContent(
                onNavigateToAnalyzer = { navController.navigate(Screen.Analyzer.route) }
            )
        }

        else -> {
            DiaryContent(
                uiState = uiState,
                onGramsChanged = { mealId, grams -> viewModel.onGramsChanged(mealId, grams) },
                onDeleteMeal = { meal -> viewModel.onDeleteMeal(meal) },
                onNavigateToAnalyzer = { navController.navigate(Screen.Analyzer.route) }
            )
        }
    }
}
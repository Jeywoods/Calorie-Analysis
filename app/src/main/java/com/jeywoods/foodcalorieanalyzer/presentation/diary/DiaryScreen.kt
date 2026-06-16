package com.jeywoods.foodcalorieanalyzer.presentation.diary

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.DeleteMealDialog
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.DiaryContent
import com.jeywoods.foodcalorieanalyzer.presentation.diary.components.EmptyDiaryContent
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.Screen
import com.jeywoods.foodcalorieanalyzer.ui.theme.Error
import com.jeywoods.foodcalorieanalyzer.ui.theme.ErrorContainer
import kotlinx.coroutines.delay

@Composable
fun DiaryScreen(
    navController: NavController,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }

    if (uiState.mealToDelete != null) {
        DeleteMealDialog(
            mealName = uiState.mealToDelete?.foodItem?.russianName ?: "",
            onConfirm = { viewModel.confirmDelete() },
            onDismiss = { viewModel.cancelDelete() }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    onNavigateToAnalyzer = { navController.navigate(Screen.Analyzer.route) },
                    onError = { errorMessage = it }
                )
            }
        }

        // Уведомление сверху
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorContainer,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        errorMessage ?: "",
                        color = Error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { errorMessage = null },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Close,
                            "Закрыть",
                            tint = Error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
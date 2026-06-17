package com.jeywoods.foodcalorieanalyzer.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeywoods.foodcalorieanalyzer.presentation.history.components.HistoryContent

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
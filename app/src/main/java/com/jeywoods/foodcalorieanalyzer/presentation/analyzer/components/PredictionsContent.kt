package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.AnalyzerUiState

@Composable
fun PredictionsContent(
    state: AnalyzerUiState.PredictionsReady,
    onPredictionSelected: (PredictionItem) -> Unit,
    onOtherVariant: () -> Unit,
    onNextStep: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Шаг 1 из 2", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("Выберите блюдо", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onCancel) { Text("Отмена", color = MaterialTheme.colorScheme.error) }
            }
        }

        HorizontalDivider()

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.predictions.forEach { prediction ->
                PredictionCard(
                    prediction = prediction,
                    isSelected = prediction == state.selectedPrediction,
                    onClick = { onPredictionSelected(prediction) }
                )
            }
            OutlinedButton(onClick = onOtherVariant, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Другое блюдо")
            }
        }

        Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 4.dp, color = MaterialTheme.colorScheme.surface) {
            Button(onClick = onNextStep, modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp),
                enabled = state.selectedPrediction != null,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text("Далее: указать вес", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}
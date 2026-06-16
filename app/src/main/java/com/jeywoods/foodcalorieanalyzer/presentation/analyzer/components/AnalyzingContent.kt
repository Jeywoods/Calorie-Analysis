package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.presentation.components.LoadingOverlay

@Composable
fun AnalyzingContent(onCancel: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingOverlay(message = "Анализируем блюдо...")
        TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
            Icon(Icons.Default.Close, null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Отмена", color = MaterialTheme.colorScheme.error)
        }
    }
}
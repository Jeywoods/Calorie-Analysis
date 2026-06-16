package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit, onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Error, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onCancel) { Text("Назад") }
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}
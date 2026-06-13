package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun GramInputSection(
    onGramsChanged: (Float) -> Unit,
    initialGrams: Float = 0f,
    modifier: Modifier = Modifier
) {
    var gramsText by remember(initialGrams) {
        mutableStateOf(if (initialGrams > 0) initialGrams.toInt().toString() else "")
    }

    OutlinedTextField(
        value = gramsText,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                gramsText = newValue
                val grams = newValue.toFloatOrNull() ?: 0f
                onGramsChanged(grams)
            }
        },
        modifier = modifier,
        label = { Text("Вес порции") },
        placeholder = { Text("100") },
        suffix = { Text("г") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}
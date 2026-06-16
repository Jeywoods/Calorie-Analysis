package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GramOption(
    val grams: Float,
    val label: String
)

@Composable
fun GramInputSection(
    modifier: Modifier = Modifier,
    onGramsChanged: (Float) -> Unit,
    initialGrams: Float = 0f
) {
    val quickOptions = listOf(
        GramOption(100f, "100"),
        GramOption(150f, "150"),
        GramOption(200f, "200"),
        GramOption(250f, "250"),
        GramOption(300f, "300")
    )

    var selectedGrams by remember { mutableFloatStateOf(initialGrams) }
    var showCustomDialog by remember { mutableStateOf(false) }

    fun selectGrams(grams: Float) {
        selectedGrams = grams
        onGramsChanged(grams)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Вес порции",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            if (selectedGrams > 0) {
                Text(
                    text = "${selectedGrams.toInt()} г",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickOptions.forEach { option ->
                val isSelected = selectedGrams == option.grams

                FilterChip(
                    selected = isSelected,
                    onClick = { selectGrams(option.grams) },
                    label = {
                        Text(
                            text = "${option.label}г",
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.weight(1f),
                    border = if (isSelected) {
                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { showCustomDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (selectedGrams > 0) "Другой вес..." else "Ввести вручную",
                fontSize = 13.sp
            )
        }
    }

    if (showCustomDialog) {
        var customGrams by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Вес порции") },
            text = {
                OutlinedTextField(
                    value = customGrams,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                            customGrams = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Введите вес в граммах") },
                    suffix = { Text("г") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val grams = customGrams.toFloatOrNull() ?: 0f
                        if (grams > 0) {
                            selectGrams(grams)
                            showCustomDialog = false
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}
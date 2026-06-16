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
    var customGrams by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun selectGrams(grams: Float) {
        selectedGrams = grams
        customGrams = ""
        showCustomInput = false
        onGramsChanged(grams)
    }

    Column(modifier = modifier) {
        Text(
            text = "Вес порции",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопки быстрого выбора
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickOptions.forEach { option ->
                val isSelected = selectedGrams == option.grams && !showCustomInput

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

        // Кастомный ввод
        if (showCustomInput) {
            OutlinedTextField(
                value = customGrams,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                        customGrams = newValue
                        val grams = newValue.toFloatOrNull() ?: 0f
                        if (grams > 0) {
                            selectedGrams = grams
                            onGramsChanged(grams)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),  // ← Минимальная высота
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )
        } else {
            TextButton(
                onClick = { showCustomInput = true },
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
    }
}
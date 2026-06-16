package com.jeywoods.foodcalorieanalyzer.presentation.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryMealCard(meal: Meal) {
    var expanded by remember { mutableStateOf(false) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.US) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: название, граммы, время, калории
            MealCardHeader(
                meal = meal,
                timeFormat = timeFormat
            )

            Spacer(modifier = Modifier.height(9.dp))
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.height(9.dp))

            // Макронутриенты (БЖУ)
            MacronutrientsRow(meal = meal)

            // Кнопка раскрытия деталей
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = if (expanded) "Скрыть детали ▲" else "Подробнее ▼",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Раскрывающиеся дополнительные нутриенты
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))

                    // Первая строка: клетчатка, сахар, натрий
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        NutrientTag(
                            "клетчатка",
                            if (meal.fiber > 0) String.format(Locale.US, "%.1f г", meal.fiber) else "—",
                            Modifier.weight(1f)
                        )
                        NutrientTag(
                            "сахар",
                            if (meal.sugar > 0) String.format(Locale.US, "%.1f г", meal.sugar) else "—",
                            Modifier.weight(1f)
                        )
                        NutrientTag(
                            "натрий",
                            if (meal.sodium > 0) String.format(Locale.US, "%.0f мг", meal.sodium) else "—",
                            Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Вторая строка: нас. жиры, калий, холестерин
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        NutrientTag(
                            "нас. жиры",
                            if (meal.saturatedFat > 0) String.format(Locale.US, "%.1f г", meal.saturatedFat) else "—",
                            Modifier.weight(1f)
                        )
                        NutrientTag(
                            "калий",
                            if (meal.potassium > 0) String.format(Locale.US, "%.0f мг", meal.potassium) else "—",
                            Modifier.weight(1f)
                        )
                        NutrientTag(
                            "холестерин",
                            if (meal.cholesterol > 0) String.format(Locale.US, "%.0f мг", meal.cholesterol) else "—",
                            Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealCardHeader(
    meal: Meal,
    timeFormat: SimpleDateFormat
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                meal.foodItem.russianName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "${meal.grams.toInt()} г · ${timeFormat.format(Date(meal.timestamp))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "${meal.calories.toInt()}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = Primary
            )
            Text(
                "ккал",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun MacronutrientsRow(meal: Meal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        HistoryMacroPill(
            "белки",
            String.format(Locale.US, "%.1f г", meal.protein),
            ProteinColor,
            Modifier.weight(1f)
        )
        HistoryMacroPill(
            "жиры",
            String.format(Locale.US, "%.1f г", meal.fat),
            FatColor,
            Modifier.weight(1f)
        )
        HistoryMacroPill(
            "углев.",
            String.format(Locale.US, "%.1f г", meal.carbs),
            CarbsColor,
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun HistoryMacroPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.09f), RoundedCornerShape(7.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = color
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = color.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun NutrientTag(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            fontSize = 9.sp
        )
    }
}
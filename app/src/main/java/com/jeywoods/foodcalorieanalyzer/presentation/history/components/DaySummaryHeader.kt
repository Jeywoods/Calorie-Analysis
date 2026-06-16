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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DaySummaryHeader(
    meals: List<Meal>,
    selectedDate: Date?
) {
    var expanded by remember { mutableStateOf(false) }

    val dateLabel = remember(selectedDate) {
        selectedDate?.let {
            SimpleDateFormat("d MMMM", Locale("ru")).format(it)
        } ?: "Выбранный день"
    }

    val totalCal = meals.sumOf { it.calories.toDouble() }.toInt()
    val totalP = meals.sumOf { it.protein.toDouble() }.toFloat()
    val totalF = meals.sumOf { it.fat.toDouble() }.toFloat()
    val totalC = meals.sumOf { it.carbs.toDouble() }.toFloat()

    // Дополнительные показатели
    val totalFiber = meals.sumOf { it.fiber.toDouble() }.toFloat()
    val totalSugar = meals.sumOf { it.sugar.toDouble() }.toFloat()
    val totalSodium = meals.sumOf { it.sodium.toDouble() }.toFloat()
    val totalSaturatedFat = meals.sumOf { it.saturatedFat.toDouble() }.toFloat()
    val totalPotassium = meals.sumOf { it.potassium.toDouble() }.toFloat()
    val totalCholesterol = meals.sumOf { it.cholesterol.toDouble() }.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3D10))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dateLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    "${meals.size} блюд",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Основные показатели (БЖУ)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DaySummaryBox("Калории", "$totalCal", "ккал", Modifier.weight(1f))
                DaySummaryBox("Белки", String.format(Locale.US, "%.1f", totalP), "г", Modifier.weight(1f))
                DaySummaryBox("Жиры", String.format(Locale.US, "%.1f", totalF), "г", Modifier.weight(1f))
                DaySummaryBox("Углеводы", String.format(Locale.US, "%.1f", totalC), "г", Modifier.weight(1f))
            }

            // Кнопка раскрытия деталей
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = if (expanded) "Скрыть детали ▲" else "Все показатели ▼",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Раскрывающиеся дополнительные показатели
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DayDetailBox(
                            label = "Клетчатка",
                            value = if (totalFiber > 0)
                                String.format(Locale.US, "%.1f", totalFiber) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DayDetailBox(
                            label = "Сахар",
                            value = if (totalSugar > 0)
                                String.format(Locale.US, "%.1f", totalSugar) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DayDetailBox(
                            label = "Натрий",
                            value = if (totalSodium > 0)
                                String.format(Locale.US, "%.0f", totalSodium) else "—",
                            unit = "мг",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Вторая строка дополнительных показателей
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DayDetailBox(
                            label = "Нас. жиры",
                            value = if (totalSaturatedFat > 0)
                                String.format(Locale.US, "%.1f", totalSaturatedFat) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DayDetailBox(
                            label = "Калий",
                            value = if (totalPotassium > 0)
                                String.format(Locale.US, "%.0f", totalPotassium) else "—",
                            unit = "мг",
                            modifier = Modifier.weight(1f)
                        )
                        DayDetailBox(
                            label = "Холестерин",
                            value = if (totalCholesterol > 0)
                                String.format(Locale.US, "%.0f", totalCholesterol) else "—",
                            unit = "мг",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DaySummaryBox(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(9.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(Modifier.width(2.dp))
            Text(
                unit,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun DayDetailBox(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 12.sp
            )
            Spacer(Modifier.width(2.dp))
            Text(
                unit,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 9.sp
            )
        }
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 9.sp
        )
    }
}
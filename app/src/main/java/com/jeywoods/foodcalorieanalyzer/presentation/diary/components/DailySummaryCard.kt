package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import java.util.Locale

@Composable
fun DailySummaryCard(summary: DailySummary) {
    var expanded by remember { mutableStateOf(false) }
    val bgColor = Color(0xFF2D5A1E)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Итого за день",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = TextUnit(0.06f, TextUnitType.Em)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Основные показатели: калории + БЖУ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryBox(
                    label = "Калории",
                    value = "${summary.totalCalories.toInt()}",
                    unit = "ккал",
                    modifier = Modifier.weight(1f)
                )
                SummaryBox(
                    label = "Белки",
                    value = String.format(Locale.US, "%.1f", summary.totalProtein),
                    unit = "г",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryBox(
                    label = "Жиры",
                    value = String.format(Locale.US, "%.1f", summary.totalFat),
                    unit = "г",
                    modifier = Modifier.weight(1f)
                )
                SummaryBox(
                    label = "Углеводы",
                    value = String.format(Locale.US, "%.1f", summary.totalCarbs),
                    unit = "г",
                    modifier = Modifier.weight(1f)
                )
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
                    Spacer(modifier = Modifier.height(8.dp))

                    // Первая строка дополнительных показателей
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DetailBox(
                            label = "Клетчатка",
                            value = if (summary.totalFiber > 0)
                                String.format(Locale.US, "%.1f", summary.totalFiber) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DetailBox(
                            label = "Сахар",
                            value = if (summary.totalSugar > 0)
                                String.format(Locale.US, "%.1f", summary.totalSugar) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DetailBox(
                            label = "Натрий",
                            value = if (summary.totalSodium > 0)
                                String.format(Locale.US, "%.0f", summary.totalSodium) else "—",
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
                        DetailBox(
                            label = "Нас. жиры",
                            value = if (summary.totalSaturatedFat > 0)
                                String.format(Locale.US, "%.1f", summary.totalSaturatedFat) else "—",
                            unit = "г",
                            modifier = Modifier.weight(1f)
                        )
                        DetailBox(
                            label = "Калий",
                            value = if (summary.totalPotassium > 0)
                                String.format(Locale.US, "%.0f", summary.totalPotassium) else "—",
                            unit = "мг",
                            modifier = Modifier.weight(1f)
                        )
                        DetailBox(
                            label = "Холестерин",
                            value = if (summary.totalCholesterol > 0)
                                String.format(Locale.US, "%.0f", summary.totalCholesterol) else "—",
                            unit = "мг",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (summary.mealCount > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Блюда ${summary.mealCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun SummaryBox(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun DetailBox(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}
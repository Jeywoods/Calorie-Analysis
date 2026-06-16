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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Заголовок
            Text(
                "Итого за день",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Калории по центру крупно
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "${summary.totalCalories.toInt()}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "ккал",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 блока Б/Ж/У
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroBox("Белки", summary.totalProtein, Modifier.weight(1f))
                MacroBox("Жиры", summary.totalFat, Modifier.weight(1f))
                MacroBox("Углеводы", summary.totalCarbs, Modifier.weight(1f))
            }

            // Кнопка деталей
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = if (expanded) "Скрыть детали ▲" else "Подробнее ▼",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            // Детали
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DetailBox("Нас. жиры", summary.totalSaturatedFat, "г", Modifier.weight(1f))
                        DetailBox("Клетчатка", summary.totalFiber, "г", Modifier.weight(1f))
                        DetailBox("Сахар", summary.totalSugar, "г", Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DetailBox("Натрий", summary.totalSodium, "мг", Modifier.weight(1f))
                        DetailBox("Калий", summary.totalPotassium, "мг", Modifier.weight(1f))
                        DetailBox("Холестерин", summary.totalCholesterol, "мг", Modifier.weight(1f))
                    }
                }
            }

            // Количество блюд внизу справа
            if (summary.mealCount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Блюд: ${summary.mealCount}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun MacroBox(
    label: String,
    value: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                String.format(Locale.US, "%.1f", value),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("г", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.65f))
        }
    }
}

@Composable
private fun DetailBox(
    label: String,
    value: Float,
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
            Text(
                text = if (value > 0) "${String.format(Locale.US, "%.1f", value)} $unit" else "—",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}
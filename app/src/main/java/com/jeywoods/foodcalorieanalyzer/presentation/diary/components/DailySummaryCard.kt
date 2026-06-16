package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DailySummaryCard(summary: DailySummary) {
    var expanded by remember { mutableStateOf(false) }
    val bgColor = Color(0xFF2D5A1E)

    val totalNutrients = summary.totalProtein + summary.totalFat + summary.totalCarbs + summary.totalFiber
    val proteinPercent = if (totalNutrients > 0) summary.totalProtein / totalNutrients else 0f
    val fatPercent = if (totalNutrients > 0) summary.totalFat / totalNutrients else 0f
    val carbsPercent = if (totalNutrients > 0) summary.totalCarbs / totalNutrients else 0f
    val fiberPercent = if (totalNutrients > 0) summary.totalFiber / totalNutrients else 0f

    val segments = listOf(
        "Белки" to (proteinPercent to ProteinColor),
        "Жиры" to (fatPercent to FatColor),
        "Углев." to (carbsPercent to CarbsColor),
        "Клетч." to (fiberPercent to Color(0xFF81C784))
    ).filter { it.second.first > 0f }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Итого за день",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Кольцо с калориями и процентами
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = size.minDimension * 0.15f
                    val radius = (size.minDimension - strokeWidth) / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val arcSize = Size(radius * 2, radius * 2)
                    val topLeft = Offset(center.x - radius, center.y - radius)

                    var startAngle = -90f

                    segments.forEach { (_, data) ->
                        val (percent, color) = data
                        val sweepAngle = percent * 360f

                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )

                        // Процент снаружи кольца
                        if (percent > 0.01f) {
                            val midAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                            val textRadius = radius + strokeWidth / 2f + 22f
                            val x = center.x + (textRadius * cos(midAngle)).toFloat()
                            val y = center.y + (textRadius * sin(midAngle)).toFloat()

                            val paint = android.graphics.Paint().apply {
                                this.color = android.graphics.Color.WHITE
                                textSize = 40f  // крупнее
                                textAlign = android.graphics.Paint.Align.CENTER
                                isAntiAlias = true
                                typeface = android.graphics.Typeface.DEFAULT_BOLD
                            }
                            drawContext.canvas.nativeCanvas.drawText(
                                "${(percent * 100).toInt()}%",
                                x, y + 10f,
                                paint
                            )
                        }

                        startAngle += sweepAngle
                    }
                }

                // Калории в центре
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${summary.totalCalories.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                    Text(
                        "ккал",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Легенда 4 элемента
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendDot("Белки", summary.totalProtein, "г", ProteinColor)
                    LegendDot("Жиры", summary.totalFat, "г", FatColor)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendDot("Углеводы", summary.totalCarbs, "г", CarbsColor)
                    LegendDot("Клетчатка", summary.totalFiber, "г", Color(0xFF81C784))
                }
            }

            // Кнопка деталей
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.align(Alignment.Start).padding(top = 2.dp)
            ) {
                Text(
                    text = if (expanded) "Скрыть детали ▲" else "Подробнее ▼",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            // Детали в 2 колонки
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DetailBox("Нас. жиры", summary.totalSaturatedFat, "г", Modifier.weight(1f))
                        DetailBox("Сахар", summary.totalSugar, "г", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DetailBox("Натрий", summary.totalSodium, "мг", Modifier.weight(1f))
                        DetailBox("Калий", summary.totalPotassium, "мг", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DetailBox("Холестерин", summary.totalCholesterol, "мг", Modifier.fillMaxWidth())
                    }
                }
            }

            // Количество блюд
            if (summary.mealCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
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
private fun LegendDot(
    label: String,
    value: Float,
    unit: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            "$label ${String.format(Locale.US, "%.1f", value)}$unit",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 11.sp
        )
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
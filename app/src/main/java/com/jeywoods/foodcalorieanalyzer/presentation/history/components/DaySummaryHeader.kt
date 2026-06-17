package com.jeywoods.foodcalorieanalyzer.presentation.history.components

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DaySummaryHeader(
    meals: List<Meal>,
    selectedDate: Date?
) {
    var expanded by remember { mutableStateOf(false) }

    val dateLabel = selectedDate?.let {
        SimpleDateFormat("d MMMM", Locale("ru")).format(it)
    } ?: "Выбранный день"

    val totalCal = meals.sumOf { it.calories.toDouble() }.toInt()
    val totalProtein = meals.sumOf { it.protein.toDouble() }.toFloat()
    val totalFat = meals.sumOf { it.fat.toDouble() }.toFloat()
    val totalCarbs = meals.sumOf { it.carbs.toDouble() }.toFloat()
    val totalFiber = meals.sumOf { it.fiber.toDouble() }.toFloat()
    val totalSugar = meals.sumOf { it.sugar.toDouble() }.toFloat()
    val totalSodium = meals.sumOf { it.sodium.toDouble() }.toFloat()
    val totalSaturatedFat = meals.sumOf { it.saturatedFat.toDouble() }.toFloat()
    val totalPotassium = meals.sumOf { it.potassium.toDouble() }.toFloat()
    val totalCholesterol = meals.sumOf { it.cholesterol.toDouble() }.toFloat()

    val totalNutrients = totalProtein + totalFat + totalCarbs
    val proteinPercent = if (totalNutrients > 0) totalProtein / totalNutrients else 0f
    val fatPercent = if (totalNutrients > 0) totalFat / totalNutrients else 0f
    val carbsPercent = if (totalNutrients > 0) totalCarbs / totalNutrients else 0f

    val segments = listOf(
        "Белки" to (proteinPercent to ProteinColor),
        "Жиры" to (fatPercent to FatColor),
        "Углев." to (carbsPercent to CarbsColor)
    ).filter { it.second.first > 0f }

    val density = LocalDensity.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3D10))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

                    val textSizePx = with(density) { 16.sp.toPx() }
                    val labelOffset = with(density) { 18.dp.toPx() }

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

                        if (percent > 0.03f) {
                            val midAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                            val textRadius = radius + strokeWidth / 2f + labelOffset

                            val x = center.x + (textRadius * cos(midAngle)).toFloat()
                            val y = center.y + (textRadius * sin(midAngle)).toFloat()

                            val paint = android.graphics.Paint().apply {
                                this.color = Color.White.toArgb()
                                this.textSize = textSizePx
                                textAlign = android.graphics.Paint.Align.CENTER
                                isAntiAlias = true
                                typeface = android.graphics.Typeface.DEFAULT_BOLD
                            }

                            drawContext.canvas.nativeCanvas.drawText(
                                "${(percent * 100).toInt()}%",
                                x,
                                y + textSizePx / 3f,
                                paint
                            )
                        }

                        startAngle += sweepAngle
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$totalCal",
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

            // Легенда — 3 элемента
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendDot("Белки", totalProtein, "г", ProteinColor)
                LegendDot("Жиры", totalFat, "г", FatColor)
                LegendDot("Углеводы", totalCarbs, "г", CarbsColor)
            }

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

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DayDetailBox("Клетчатка", if (totalFiber > 0) String.format(Locale.US, "%.1f", totalFiber) else "—", "г", Modifier.weight(1f))
                        DayDetailBox("Нас. жиры", if (totalSaturatedFat > 0) String.format(Locale.US, "%.1f", totalSaturatedFat) else "—", "г", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DayDetailBox("Сахар", if (totalSugar > 0) String.format(Locale.US, "%.1f", totalSugar) else "—", "г", Modifier.weight(1f))
                        DayDetailBox("Натрий", if (totalSodium > 0) String.format(Locale.US, "%.0f", totalSodium) else "—", "мг", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DayDetailBox("Калий", if (totalPotassium > 0) String.format(Locale.US, "%.0f", totalPotassium) else "—", "мг", Modifier.weight(1f))
                        DayDetailBox("Холестерин", if (totalCholesterol > 0) String.format(Locale.US, "%.0f", totalCholesterol) else "—", "мг", Modifier.weight(1f))
                    }
                }
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
private fun DayDetailBox(
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
            Text(
                text = "$value $unit",
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
package com.jeywoods.foodcalorieanalyzer.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.ui.theme.Primary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalorieCalendar(
    dailySummaries: List<DailySummary>,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit
) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }

    val summaryMap = remember(dailySummaries) {
        dailySummaries.associateBy { summary ->
            Calendar.getInstance().apply {
                timeInMillis = try {
                    summary.date.toLong()
                } catch (e: NumberFormatException) {
                    SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(summary.date)!!.time
                }
            }.let {
                Triple(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH))
            }
        }
    }

    val today = remember { Calendar.getInstance() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // — Навигация —
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                    },
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Outlined.ChevronLeft, null, modifier = Modifier.size(16.dp))
                }

                Text(
                    text = buildString {
                        val monthNames = listOf(
                            "Январь","Февраль","Март","Апрель","Май","Июнь",
                            "Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"
                        )
                        append(monthNames[currentMonth.get(Calendar.MONTH)])
                        append(" ")
                        append(currentMonth.get(Calendar.YEAR))
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                OutlinedButton(
                    onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                    },
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // — Заголовки дней недели (Пн-Вс) —
            val weekDays = listOf("Пн","Вт","Ср","Чт","Пт","Сб","Вс")
            Row(modifier = Modifier.fillMaxWidth()) {
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // — Сетка дней —
            val firstDay = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
            // Конвертируем Sunday=1..Saturday=7 → Mon=0..Sun=6
            val startOffset = ((firstDay.get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7)
            val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            val year = currentMonth.get(Calendar.YEAR)
            val month = currentMonth.get(Calendar.MONTH)

            val totalCells = startOffset + daysInMonth
            val rows = (totalCells + 6) / 7

            repeat(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { col ->
                        val idx = row * 7 + col
                        val day = idx - startOffset + 1
                        if (day < 1 || day > daysInMonth) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val key = Triple(year, month, day)
                            val summary = summaryMap[key]
                            val isToday = year == today.get(Calendar.YEAR) &&
                                    month == today.get(Calendar.MONTH) &&
                                    day == today.get(Calendar.DAY_OF_MONTH)
                            val isSelected = selectedDate?.let {
                                val sc = Calendar.getInstance().apply { time = it }
                                year == sc.get(Calendar.YEAR) &&
                                        month == sc.get(Calendar.MONTH) &&
                                        day == sc.get(Calendar.DAY_OF_MONTH)
                            } ?: false

                            DayCell(
                                day = day,
                                isToday = isToday,
                                isSelected = isSelected,
                                hasData = summary != null,
                                calories = summary?.totalCalories?.toInt(),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    val date = Calendar.getInstance().apply {
                                        set(year, month, day, 0, 0, 0)
                                        set(Calendar.MILLISECOND, 0)
                                    }.time
                                    onDateSelected(date)
                                }
                            )
                        }
                    }
                }
                if (row < rows - 1) Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isToday: Boolean,
    isSelected: Boolean,
    hasData: Boolean,
    calories: Int?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> Primary
        else -> Color.Transparent
    }
    val numColor = when {
        isSelected -> Color.White
        isToday -> Primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable(enabled = hasData || isToday, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isToday || isSelected) FontWeight.Medium else FontWeight.Normal,
            color = numColor,
            fontSize = 13.sp
        )
        if (hasData && calories != null) {
            // индикатор-точка
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.6f) else Primary.copy(alpha = 0.7f)
                    )
            )
        } else {
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}
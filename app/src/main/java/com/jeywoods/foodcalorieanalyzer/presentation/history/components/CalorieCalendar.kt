package com.jeywoods.foodcalorieanalyzer.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalorieCalendar(
    dailySummaries: List<DailySummary>,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val monthFormat = SimpleDateFormat("LLLL yyyy", Locale("ru"))

    val today = Calendar.getInstance()
    val startCal = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -30)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // Create list of days
    val days = mutableListOf<Date>()
    val tempCal = Calendar.getInstance().apply {
        time = startCal.time
    }

    for (i in 0..30) {
        days.add(tempCal.time)
        tempCal.add(Calendar.DAY_OF_YEAR, 1)
    }

    // Group into weeks
    val firstDayCal = Calendar.getInstance().apply {
        time = days.first()
    }
    val firstDayOfWeek = (firstDayCal.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Monday = 0

    val weeks = mutableListOf<List<Date?>>()
    var currentWeek = mutableListOf<Date?>()

    // Add padding for first week
    repeat(firstDayOfWeek) {
        currentWeek.add(null)
    }

    days.forEach { date ->
        currentWeek.add(date)
        if (currentWeek.size == 7) {
            weeks.add(currentWeek.toList())
            currentWeek = mutableListOf()
        }
    }

    if (currentWeek.isNotEmpty()) {
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(currentWeek)
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Month header
        Text(
            text = monthFormat.format(startCal.time).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString()
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Day of week headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            val dateString = dateFormat.format(date)
                            val summary = dailySummaries.find { it.date == dateString }
                            val color = getDayColor(summary)
                            val isSelected = selectedDate != null &&
                                    dateFormat.format(date) == dateFormat.format(selectedDate)

                            val dayCal = Calendar.getInstance().apply { time = date }
                            val todayCal = Calendar.getInstance()
                            val isToday = dateFormat.format(date) == dateFormat.format(todayCal.time)

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else color.copy(alpha = 0.3f)
                                    )
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = dayCal.get(Calendar.DAY_OF_MONTH).toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )

                                    if (summary != null) {
                                        Text(
                                            text = "${summary.totalCalories.toInt()}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 8.sp
                                            ),
                                            color = if (isSelected)
                                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                            else
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(color = CalendarGreen, label = "< 1500")
            LegendItem(color = CalendarYellow, label = "1500-2000")
            LegendItem(color = CalendarOrange, label = "2000-2500")
            LegendItem(color = CalendarRed, label = "> 2500")
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun getDayColor(summary: DailySummary?): Color {
    if (summary == null || summary.meals.isEmpty()) return CalendarGray

    return when {
        summary.totalCalories < 1500 -> CalendarGreen
        summary.totalCalories < 2000 -> CalendarYellow
        summary.totalCalories < 2500 -> CalendarOrange
        else -> CalendarRed
    }
}
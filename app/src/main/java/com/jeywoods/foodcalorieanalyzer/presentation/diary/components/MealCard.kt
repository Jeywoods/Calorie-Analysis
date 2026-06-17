package com.jeywoods.foodcalorieanalyzer.presentation.diary.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MealCard(
    meal: Meal,
    onGramsChanged: (Float) -> Unit,
    onDelete: () -> Unit,
    onEditingChanged: (Boolean) -> Unit = {},
    onError: (String) -> Unit = {},
    listState: LazyListState? = null,
    itemIndex: Int = -1
) {
    var isEditing by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var gramsText by remember(meal.grams) { mutableStateOf(meal.grams.toInt().toString()) }
    val coroutineScope = rememberCoroutineScope()

    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.US) }
    val mealTime = remember(meal.timestamp) { timeFormat.format(Date(meal.timestamp)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            MealHeader(meal, mealTime)

            Spacer(modifier = Modifier.height(12.dp))

            MacroPillsRow(meal)

            ExpandableDetails(expanded = expanded, onToggle = { expanded = !expanded }, meal = meal)

            Spacer(modifier = Modifier.height(8.dp))

            MealActions(
                isEditing = isEditing,
                gramsText = gramsText,
                onGramsTextChanged = { gramsText = it },
                onEditClick = {
                    onEditingChanged(true)
                    coroutineScope.launch {
                        if (itemIndex >= 0) {
                            listState?.animateScrollToItem(itemIndex, scrollOffset = -200)
                        }
                        isEditing = true
                    }
                },
                onSaveClick = { grams ->
                    onGramsChanged(grams)
                    onEditingChanged(false)
                    isEditing = false
                },
                onDelete = onDelete
            )
        }
    }
}

@Composable
private fun MealHeader(meal: Meal, mealTime: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MealThumbnail(meal)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                meal.foodItem.russianName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "${meal.grams.toInt()} г · $mealTime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        Text(
            text = "${meal.calories.toInt()} ккал",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
    }
}

@Composable
private fun MealThumbnail(meal: Meal) {
    if (meal.imagePath != null && File(meal.imagePath).exists()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(File(meal.imagePath)).crossfade(true).build(),
            contentDescription = meal.foodItem.russianName,
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) { Text("🍽", fontSize = 22.sp) }
    }
}

@Composable
private fun MacroPillsRow(meal: Meal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        MacroPill("белки",   String.format(Locale.US, "%.1f г", meal.protein), ProteinColor, Modifier.weight(1f))
        MacroPill("жиры",   String.format(Locale.US, "%.1f г", meal.fat),     FatColor,     Modifier.weight(1f))
        MacroPill("углев.", String.format(Locale.US, "%.1f г", meal.carbs),   CarbsColor,   Modifier.weight(1f))
    }
}

@Composable
private fun ExpandableDetails(
    expanded: Boolean,
    onToggle: () -> Unit,
    meal: Meal
) {
    TextButton(
        onClick = onToggle,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (expanded) "Скрыть детали ▲" else "Подробнее ▼",
            style = MaterialTheme.typography.labelMedium
        )
    }

    AnimatedVisibility(visible = expanded) {
        Column {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                ExtraTag("клетчатка", if (meal.fiber > 0) String.format(Locale.US, "%.1f г", meal.fiber) else "—", Modifier.weight(1f))
                ExtraTag("сахар", if (meal.sugar > 0) String.format(Locale.US, "%.1f г", meal.sugar) else "—", Modifier.weight(1f))
                ExtraTag("натрий", if (meal.sodium > 0) String.format(Locale.US, "%.0f мг", meal.sodium) else "—", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                ExtraTag("нас. жиры", if (meal.saturatedFat > 0) String.format(Locale.US, "%.1f г", meal.saturatedFat) else "—", Modifier.weight(1f))
                ExtraTag("калий", if (meal.potassium > 0) String.format(Locale.US, "%.0f мг", meal.potassium) else "—", Modifier.weight(1f))
                ExtraTag("холестерин", if (meal.cholesterol > 0) String.format(Locale.US, "%.0f мг", meal.cholesterol) else "—", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MacroPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.09f))
            .padding(horizontal = 8.dp, vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.75f))
    }
}

@Composable
fun ExtraTag(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            fontSize = 10.sp)
    }
}
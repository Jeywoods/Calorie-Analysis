package com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeywoods.foodcalorieanalyzer.ui.theme.Primary

@Composable
fun IdleContent(onTakePhoto: () -> Unit, onChooseFromGallery: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Сфотографируйте блюдо", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Мы определим что это за блюдо и посчитаем калории", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Как это работает", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.height(16.dp))
                HowItWorksStep("1", "Сделайте фото", "Сфотографируйте блюдо или выберите из галереи")
                Spacer(modifier = Modifier.height(12.dp))
                HowItWorksStep("2", "Подтвердите блюдо", "Выберите правильный вариант из предложенных")
                Spacer(modifier = Modifier.height(12.dp))
                HowItWorksStep("3", "Укажите вес", "Введите граммовку и добавьте в дневник")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onTakePhoto, modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary), shape = MaterialTheme.shapes.medium) {
            Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Камера", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onChooseFromGallery, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = MaterialTheme.shapes.medium) {
            Icon(Icons.Default.PhotoLibrary, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Галерея", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun HowItWorksStep(number: String, title: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Surface(modifier = Modifier.size(28.dp), shape = MaterialTheme.shapes.small, color = Primary) {
            Box(contentAlignment = Alignment.Center) {
                Text(number, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(description, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
        }
    }
}
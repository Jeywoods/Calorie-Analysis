package com.jeywoods.foodcalorieanalyzer.presentation.analyzer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.domain.model.PredictionItem
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components.FoodSearchDialog
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components.GramInputSection
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components.ImageCropperDialog
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components.PredictionCard
import com.jeywoods.foodcalorieanalyzer.presentation.components.LoadingOverlay
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.Screen
import com.jeywoods.foodcalorieanalyzer.util.ImageUtils
import java.io.File
import java.util.Locale

@Composable
fun AnalyzerScreen(
    navController: NavController,
    viewModel: AnalyzerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showFoodSearchDialog by remember { mutableStateOf(false) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var imageToClassify by remember { mutableStateOf<Bitmap?>(null) }
    var showCropper by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempPhotoUri?.let { uri ->
                val bitmap = ImageUtils.rotateImageIfNeeded(context, uri)
                bitmap?.let {
                    imageToClassify = it
                    showCropper = true
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = ImageUtils.getBitmapFromUri(context, it)
            bitmap?.let {
                imageToClassify = it
                showCropper = true
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            tempPhotoUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
            tempPhotoUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Разрешение на камеру необходимо", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is AnalyzerUiState.Idle -> {
                IdleContent(
                    onTakePhoto = {
                        when {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                                val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                                tempPhotoUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
                                tempPhotoUri?.let { cameraLauncher.launch(it) }
                            }
                            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    onChooseFromGallery = { galleryLauncher.launch("image/*") }
                )
            }

            is AnalyzerUiState.Analyzing -> {
                AnalyzingContent(onCancel = { viewModel.resetState() })
            }

            is AnalyzerUiState.PredictionsReady -> {
                PredictionsContent(
                    state = state,
                    onPredictionSelected = { viewModel.onPredictionSelected(it) },
                    onOtherVariant = { showFoodSearchDialog = true },
                    onGramsChanged = { viewModel.onGramsChanged(it) },
                    onAddToDiary = {
                        Toast.makeText(context, "Введите вес блюда", Toast.LENGTH_SHORT).show()
                    },
                    onCancel = { viewModel.resetState() }
                )
            }

            is AnalyzerUiState.NutritionCalculated -> {
                NutritionContent(
                    state = state,
                    onPredictionSelected = { viewModel.onPredictionSelected(it) },
                    onOtherVariant = { showFoodSearchDialog = true },
                    onGramsChanged = { viewModel.onGramsChanged(it) },
                    onAddToDiary = {
                        val success = viewModel.onAddToDiary()
                        if (success) {
                            navController.navigate(Screen.Diary.route) {
                                popUpTo(Screen.Analyzer.route) { inclusive = true }
                            }
                        }
                    },
                    onCancel = { viewModel.resetState() }
                )
            }

            is AnalyzerUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.clearError() },
                    onCancel = { viewModel.resetState() }
                )
            }

            is AnalyzerUiState.MealAdded -> { }
        }
    }

    if (showFoodSearchDialog) {
        FoodSearchDialog(
            onDismiss = { showFoodSearchDialog = false },
            onFoodSelected = { foodItem ->
                viewModel.onManualFoodSelected(foodItem)
                showFoodSearchDialog = false
            },
            searchFoods = { query -> viewModel.onSearchFood(query) }
        )
    }

    if (showCropper && imageToClassify != null) {
        ImageCropperDialog(
            bitmap = imageToClassify!!,
            onCropComplete = { croppedBitmap ->
                viewModel.onImageSelected(croppedBitmap)
                showCropper = false
                imageToClassify = null
            },
            onDismiss = {
                showCropper = false
                imageToClassify = null
            }
        )
    }
}

@Composable
private fun IdleContent(onTakePhoto: () -> Unit, onChooseFromGallery: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Restaurant, null, Modifier.size(100.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Анализ калорий", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Сфотографируйте блюдо или выберите из галереи", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onTakePhoto, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
            Icon(Icons.Default.CameraAlt, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Сделать фото")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onChooseFromGallery, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Icon(Icons.Default.PhotoLibrary, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выбрать из галереи")
        }
    }
}

@Composable
private fun AnalyzingContent(onCancel: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingOverlay(message = "Анализируем блюдо...")
        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Icon(Icons.Default.Close, null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Отмена", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun PredictionsContent(
    state: AnalyzerUiState.PredictionsReady,
    onPredictionSelected: (PredictionItem) -> Unit,
    onOtherVariant: () -> Unit,
    onGramsChanged: (Float) -> Unit,
    onAddToDiary: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Выберите блюдо", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onCancel) {
                    Text("Отмена", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.predictions.forEach { prediction ->
                PredictionCard(
                    prediction = prediction,
                    isSelected = prediction == state.selectedPrediction,
                    onClick = { onPredictionSelected(prediction) }
                )
            }

            OutlinedButton(
                onClick = onOtherVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Другое блюдо")
            }
        }

        if (state.selectedPrediction != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    GramInputSection(onGramsChanged = onGramsChanged)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onAddToDiary,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить в дневник", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun NutritionContent(
    state: AnalyzerUiState.NutritionCalculated,
    onPredictionSelected: (PredictionItem) -> Unit,
    onOtherVariant: () -> Unit,
    onGramsChanged: (Float) -> Unit,
    onAddToDiary: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Выберите блюдо", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onCancel) {
                    Text("Отмена", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Пищевая ценность", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${state.grams.toInt()}г", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.7f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        NutritionItem("Калории", "${state.calculatedCalories.toInt()}", "ккал", Color.White)
                        NutritionItem("Белки", String.format(Locale.US, "%.1f", state.calculatedProtein), "г", Color.White)
                        NutritionItem("Жиры", String.format(Locale.US, "%.1f", state.calculatedFat), "г", Color.White)
                        NutritionItem("Углеводы", String.format(Locale.US, "%.1f", state.calculatedCarbs), "г", Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Варианты:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            state.predictions.forEach { prediction ->
                PredictionCard(
                    prediction = prediction,
                    isSelected = prediction == state.selectedPrediction,
                    onClick = { onPredictionSelected(prediction) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = onOtherVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Другое блюдо")
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GramInputSection(
                    onGramsChanged = onGramsChanged,
                    initialGrams = state.grams,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onAddToDiary,
                    modifier = Modifier.height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("В дневник", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun NutritionItem(label: String, value: String, unit: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(unit, style = MaterialTheme.typography.bodySmall, color = color.copy(alpha = 0.8f))
        Text(label, style = MaterialTheme.typography.bodySmall, color = color.copy(alpha = 0.7f))
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Error, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onCancel) { Text("Назад") }
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}
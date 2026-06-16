package com.jeywoods.foodcalorieanalyzer.presentation.analyzer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.components.*
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.Screen
import com.jeywoods.foodcalorieanalyzer.util.ImageUtils
import java.io.File

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
                    onNextStep = { viewModel.goToGramsStep() },
                    onCancel = { viewModel.resetState() }
                )
            }

            is AnalyzerUiState.NutritionCalculated -> {
                // Используем новую версию из components пакета
                NutritionContent(
                    state = state,
                    onGramsChanged = { viewModel.onGramsChanged(it) },
                    onAddToDiary = {
                        val success = viewModel.onAddToDiary()
                        if (success) {
                            navController.navigate(Screen.Diary.route) {
                                popUpTo(Screen.Analyzer.route) { inclusive = true }
                            }
                        }
                    },
                    onBackToSelect = { viewModel.goBackToSelectFood() },
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

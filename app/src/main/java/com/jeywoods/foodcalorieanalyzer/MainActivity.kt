package com.jeywoods.foodcalorieanalyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.jeywoods.foodcalorieanalyzer.presentation.navigation.NavGraph
import com.jeywoods.foodcalorieanalyzer.presentation.onboarding.OnboardingManager
import com.jeywoods.foodcalorieanalyzer.presentation.onboarding.OnboardingScreen
import com.jeywoods.foodcalorieanalyzer.ui.theme.FoodAnalyzerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FoodAnalyzerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    var showOnboarding by remember {
                        mutableStateOf(!OnboardingManager.isOnboardingDone(context))
                    }

                    if (showOnboarding) {
                        OnboardingScreen(
                            onFinish = {
                                OnboardingManager.setOnboardingDone(context)
                                showOnboarding = false
                            }
                        )
                    } else {
                        val navController = rememberNavController()
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
package com.jeywoods.foodcalorieanalyzer.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jeywoods.foodcalorieanalyzer.presentation.analyzer.AnalyzerScreen
import com.jeywoods.foodcalorieanalyzer.presentation.components.AppBottomBar
import com.jeywoods.foodcalorieanalyzer.presentation.components.AppTopBar
import com.jeywoods.foodcalorieanalyzer.presentation.diary.DiaryScreen
import com.jeywoods.foodcalorieanalyzer.presentation.history.HistoryScreen
import com.jeywoods.foodcalorieanalyzer.presentation.history.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

sealed class Screen(val route: String, val title: String, val index: Int) {
    object Analyzer : Screen("analyzer", "Анализ калорий", 0)
    object Diary : Screen("diary", "Дневник питания", 1)
    object History : Screen("history", "История", 2)
}

@Composable
fun NavGraph(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var previousIndex by remember { mutableIntStateOf(0) }

    val currentScreen = when (currentRoute) {
        Screen.Analyzer.route -> Screen.Analyzer
        Screen.Diary.route -> Screen.Diary
        Screen.History.route -> Screen.History
        else -> Screen.Analyzer
    }

    val isForward = currentScreen.index >= previousIndex

    LaunchedEffect(currentRoute) {
        previousIndex = currentScreen.index
    }

    // Дата для подзаголовка дневника
    val diarySubtitle = remember {
        val sdf = SimpleDateFormat("EEE, d MMMM yyyy", Locale("ru"))
        sdf.format(Date()).replaceFirstChar { it.titlecase(Locale("ru")) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = currentScreen.title,
                subtitle = if (currentRoute == Screen.Diary.route) diarySubtitle else null,
                actions = {
                    if (currentRoute == Screen.History.route) {
                        val historyViewModel: HistoryViewModel = hiltViewModel()
                        IconButton(onClick = { historyViewModel.exportToCsv() }) {
                            Icon(
                                Icons.Outlined.FileDownload,
                                contentDescription = "Экспорт CSV",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Analyzer.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth ->
                        if (isForward) fullWidth else -fullWidth
                    }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth ->
                        if (isForward) -fullWidth else fullWidth
                    }
                )
            }
        ) {
            composable(route = Screen.Analyzer.route) {
                AnalyzerScreen(navController)
            }
            composable(route = Screen.Diary.route) {
                DiaryScreen(navController)
            }
            composable(route = Screen.History.route) {
                HistoryScreen()
            }
        }
    }
}
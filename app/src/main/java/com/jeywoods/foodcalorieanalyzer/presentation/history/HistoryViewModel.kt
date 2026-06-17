package com.jeywoods.foodcalorieanalyzer.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.usecase.GetDailyMealsUseCase
import com.jeywoods.foodcalorieanalyzer.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getDailyMealsUseCase: GetDailyMealsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getHistoryUseCase().collect { summaries ->
                _uiState.update { state ->
                    state.copy(
                        dailySummaries = summaries,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onDateSelected(date: Date) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(selectedDate = date) }
            getDailyMealsUseCase(date).collect { meals ->
                _uiState.update { state -> state.copy(selectedDayMeals = meals) }
            }
        }
    }
}

data class HistoryUiState(
    val dailySummaries: List<DailySummary> = emptyList(),
    val selectedDate: Date? = null,
    val selectedDayMeals: List<Meal> = emptyList(),
    val isLoading: Boolean = true
)
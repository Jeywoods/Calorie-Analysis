package com.jeywoods.foodcalorieanalyzer.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.usecase.ExportHistoryUseCase
import com.jeywoods.foodcalorieanalyzer.domain.usecase.GetDailyMealsUseCase
import com.jeywoods.foodcalorieanalyzer.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getDailyMealsUseCase: GetDailyMealsUseCase,
    private val exportHistoryUseCase: ExportHistoryUseCase
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
            getDailyMealsUseCase(date).collect { meals ->
                _uiState.update { state ->
                    state.copy(selectedDayMeals = meals)
                }
            }
        }
    }

    fun exportToCsv() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isExporting = true) }
                val file = exportHistoryUseCase()
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        exportedFile = file
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        error = e.message ?: "Ошибка экспорта"
                    )
                }
            }
        }
    }

    fun clearExportedFile() {
        _uiState.update { it.copy(exportedFile = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class HistoryUiState(
    val dailySummaries: List<DailySummary> = emptyList(),
    val selectedDayMeals: List<Meal> = emptyList(),
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val exportedFile: File? = null,
    val error: String? = null
)
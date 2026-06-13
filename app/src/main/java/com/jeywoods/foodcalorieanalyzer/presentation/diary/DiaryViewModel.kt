package com.jeywoods.foodcalorieanalyzer.presentation.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeywoods.foodcalorieanalyzer.domain.model.DailySummary
import com.jeywoods.foodcalorieanalyzer.domain.model.Meal
import com.jeywoods.foodcalorieanalyzer.domain.usecase.DeleteMealUseCase
import com.jeywoods.foodcalorieanalyzer.domain.usecase.GetDailyMealsUseCase
import com.jeywoods.foodcalorieanalyzer.domain.usecase.UpdateMealGramsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getDailyMealsUseCase: GetDailyMealsUseCase,
    private val updateMealGramsUseCase: UpdateMealGramsUseCase,
    private val deleteMealUseCase: DeleteMealUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(getTodayDate())

    init {
        loadDailyMeals()
    }

    private fun getTodayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun loadDailyMeals() {
        viewModelScope.launch {
            getDailyMealsUseCase(_selectedDate.value).collect { meals ->
                _uiState.update { state ->
                    state.copy(
                        meals = meals,
                        isLoading = false
                    )
                }
            }
        }

        viewModelScope.launch {
            getDailyMealsUseCase.getDailySummary(_selectedDate.value).collect { summary ->
                _uiState.update { state ->
                    state.copy(dailySummary = summary)
                }
            }
        }
    }

    fun onGramsChanged(mealId: Long, grams: Float) {
        viewModelScope.launch {
            try {
                updateMealGramsUseCase(mealId, grams)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun onDeleteMeal(meal: Meal) {
        _uiState.update { it.copy(mealToDelete = meal) }
    }

    fun confirmDelete() {
        val mealToDelete = _uiState.value.mealToDelete ?: return

        viewModelScope.launch {
            try {
                deleteMealUseCase(mealToDelete.id)
                _uiState.update { it.copy(mealToDelete = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        mealToDelete = null,
                        error = e.message
                    )
                }
            }
        }
    }

    fun cancelDelete() {
        _uiState.update { it.copy(mealToDelete = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onDateSelected(date: Date) {
        _selectedDate.value = date
        loadDailyMeals()
    }
}

data class DiaryUiState(
    val meals: List<Meal> = emptyList(),
    val dailySummary: DailySummary? = null,
    val isLoading: Boolean = true,
    val mealToDelete: Meal? = null,
    val error: String? = null
)
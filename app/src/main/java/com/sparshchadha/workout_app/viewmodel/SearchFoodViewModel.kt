package com.sparshchadha.workout_app.viewmodel

import android.adservices.adselection.RemoveAdSelectionOverrideRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchFoodViewModellll"

@HiltViewModel
class SearchFoodViewModel @Inject constructor(
    private val foodItemsRepository: FoodItemsRepository,
    private val pexelsRepository: PexelsRepository,
) : ViewModel() {
    private val _foodItems = mutableStateOf<NutritionalValueDto?>(null)
    val foodItems = _foodItems

    private val _searchQuery = mutableStateOf("")

    private val _uiEventState = MutableStateFlow<WorkoutViewModel.UIEvent?>(value = null)
    val uiEventStateFlow = _uiEventState.asStateFlow()


    fun getFoodItems() {
        viewModelScope.launch {
            val nutritionalValues = foodItemsRepository.getFoodItems(foodSearchQuery = _searchQuery.value)
            nutritionalValues.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e(TAG, "getFoodItems: response - ${result.data}")
                        _foodItems.value = result.data
                        _uiEventState.emit(
                            WorkoutViewModel.UIEvent.HideLoader
                        )
                    }

                    is Resource.Loading -> {
                        _uiEventState.emit(
                            WorkoutViewModel.UIEvent.ShowLoader
                        )
                    }
                    is Resource.Error -> {
                        _uiEventState.emit(
                            WorkoutViewModel.UIEvent.ShowError(errorMessage = result.error?.message.toString())
                        )

                    }
                }
            }
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
}
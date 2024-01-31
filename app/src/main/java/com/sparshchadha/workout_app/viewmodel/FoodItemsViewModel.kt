package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchFoodViewModellll"

@HiltViewModel
class FoodItemsViewModel @Inject constructor(
    private val foodItemsRepository: FoodItemsRepository,
    private val pexelsRepository: PexelsRepository
) : ViewModel() {
    private val _foodItemsFromApi = mutableStateOf<NutritionalValueDto?>(null)
    val foodItemsFromApi = _foodItemsFromApi

    private val _searchQuery = mutableStateOf("")

    private val _uiEventState = MutableStateFlow<WorkoutViewModel.UIEvent?>(value = null)
    val uiEventStateFlow = _uiEventState.asStateFlow()

    private val _savedFoodItems = mutableStateOf<List<FoodItemEntity>?>(null)
    val savedFoodItems = _savedFoodItems

    private val _caloriesGaol: MutableState<String?> = mutableStateOf("999")
    val caloriesGoal = _caloriesGaol

    fun getFoodItemsFromApi() {
        viewModelScope.launch {
            val nutritionalValues = foodItemsRepository.getFoodItems(foodSearchQuery = _searchQuery.value)
            nutritionalValues.collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e(TAG, "getFoodItems: response - ${result.data}")
                        _foodItemsFromApi.value = result.data
                        _uiEventState.emit(
                            WorkoutViewModel.UIEvent.HideLoaderAndShowResponse
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

    fun saveFoodItem(foodItemEntity: FoodItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.saveFoodItem(foodItemEntity)
        }
    }

    fun getFoodItemsConsumedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second
    ) {
        viewModelScope.launch (Dispatchers.IO) {
            val foodItems = foodItemsRepository.getFoodItemsConsumedOn(date, month)
            foodItems.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _savedFoodItems.value = response.data
                        _uiEventState.value = WorkoutViewModel.UIEvent.HideLoaderAndShowResponse
                    }

                    is Resource.Loading -> {
                        _uiEventState.value = WorkoutViewModel.UIEvent.ShowLoader
                    }

                    is Resource.Error -> {
                        _uiEventState.value = response.error?.message?.let {
                            WorkoutViewModel.UIEvent.ShowError(
                                errorMessage = it
                            )
                        }
                    }
                }
            }
        }
    }

    fun addOrUpdateCaloriesGoal(caloriesGoal: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.saveOrUpdateCaloriesGoal(caloriesGoal = caloriesGoal.toString())
        }
    }

    fun getCaloriesGoal() {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.getCaloriesGoal()
                .catch { e ->
                    Log.e(TAG, "Error getting calories goal: ${e.message}")
                }
                .collect { value ->
                    _caloriesGaol.value = value
                    Log.d(TAG, "Calories goal: $value")
                }
        }
    }
}
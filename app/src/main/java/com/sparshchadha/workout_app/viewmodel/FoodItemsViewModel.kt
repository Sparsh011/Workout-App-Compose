package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.util.Constants
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "SearchFoodViewModellll"

@HiltViewModel
class FoodItemsViewModel @Inject constructor(
    private val foodItemsRepository: FoodItemsRepository,
    private val pexelsRepository: PexelsRepository,
) : ViewModel() {
    private val _foodItemsFromApi = mutableStateOf<NutritionalValueDto?>(null)
    val foodItemsFromApi = _foodItemsFromApi

    private val _searchQuery = mutableStateOf("")

    private val _uiEventState = MutableStateFlow<WorkoutViewModel.UIEvent?>(value = null)
    val uiEventStateFlow = _uiEventState.asStateFlow()

    private val _consumedFoodItems = MutableStateFlow<List<FoodItemEntity>?>(null)
    val consumedFoodItems = _consumedFoodItems

    private val _caloriesConsumed: MutableState<String?> = mutableStateOf(null)
    val caloriesConsumed = _caloriesConsumed

    private val _nutrientsConsumed: MutableMap<String, Double> = mutableStateMapOf()
    val nutrientsConsumed = _nutrientsConsumed

    private val _selectedDateAndMonthForFoodItems = MutableStateFlow<Pair<Int, String>?>(null)
    val selectedDateAndMonthForFoodItems = _selectedDateAndMonthForFoodItems.asStateFlow()

    private val _foodItemEntity = mutableStateOf<FoodItemEntity?>(null)
    val foodItemEntity = _foodItemEntity

    private val _selectedFoodItem = mutableStateOf<FoodItem?>(null)
    val selectedFoodItem = _selectedFoodItem

    private val _savedFoodItems = MutableStateFlow<List<FoodItemEntity>?>(null)
    val savedFoodItems = _savedFoodItems.asStateFlow()

    private val _selectedDayPair = mutableStateOf(
        Pair(
            HelperFunctions.getCurrentDateAndMonth().second,
            HelperFunctions.getCurrentDateAndMonth().first
        )
    )
    val selectedDayPosition = _selectedDayPair

    fun getFoodItemsFromApi() {
        viewModelScope.launch {
            try {
                val nutritionalValues =
                    foodItemsRepository.getFoodItemsFromApi(foodSearchQuery = _searchQuery.value)
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
//                        _uiEventState.emit(
//                            WorkoutViewModel.UIEvent.ShowError(errorMessage = result.error?.message.toString())
//                        )

                        }
                    }
                }
            } catch (_: Exception) {
                _uiEventState.emit(
                    WorkoutViewModel.UIEvent.ShowError(errorMessage = "Something Went Wrong!")
                )
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
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val foodItems = foodItemsRepository.getFoodItemsConsumedOn(date, month)
            foodItems.collect { response ->
                _consumedFoodItems.emit(response)
                withContext(Dispatchers.Main) {
                    getTotalCaloriesConsumed(foodItemsConsumed = response)
                    getNutrientsConsumed(foodItemsConsumed = response)
                }
                _uiEventState.emit(WorkoutViewModel.UIEvent.HideLoaderAndShowResponse)
                _selectedDateAndMonthForFoodItems.emit(Pair(date.toInt(), month))
                _selectedDayPair.value = Pair(month, date.toInt())
            }
        }
    }

    private fun getNutrientsConsumed(foodItemsConsumed: List<FoodItemEntity>?) {
        _nutrientsConsumed.clear()

        if (foodItemsConsumed != null) {
            for (foodItemEntity in foodItemsConsumed) {
                val foodItemDetails = foodItemEntity.foodItemDetails

                if (foodItemDetails != null) {
                    _nutrientsConsumed[Constants.CARBOHYDRATES_TOTAL_G] =
                        (_nutrientsConsumed[Constants.CARBOHYDRATES_TOTAL_G]
                            ?: 0.0) + (foodItemDetails.carbohydrates_total_g * foodItemEntity.servings)

                    _nutrientsConsumed[Constants.FAT_TOTAL_G] =
                        (_nutrientsConsumed[Constants.FAT_TOTAL_G]
                            ?: 0.0) + (foodItemDetails.fat_total_g * foodItemEntity.servings)

                    _nutrientsConsumed[Constants.PROTEIN_G] =
                        (_nutrientsConsumed[Constants.PROTEIN_G]
                            ?: 0.0) + (foodItemDetails.protein_g * foodItemEntity.servings)

                }
            }

            _nutrientsConsumed[Constants.PROTEIN_G]?.let { protein ->
                _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] = protein
            }

            _nutrientsConsumed[Constants.CARBOHYDRATES_TOTAL_G]?.let { carbs ->
                if (_nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] != null) {
                    _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] =
                        _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G]!! + carbs
                } else {
                    _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] = carbs
                }
            }

            _nutrientsConsumed[Constants.FAT_TOTAL_G]?.let { fats ->
                if (_nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] != null) {
                    _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] =
                        _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G]!! + fats
                } else {
                    _nutrientsConsumed[Constants.TOTAL_NUTRIENTS_G] = fats
                }
            }
        }

        if (_nutrientsConsumed.isEmpty()) {
            _nutrientsConsumed[Constants.CARBOHYDRATES_TOTAL_G] = 0.0
            _nutrientsConsumed[Constants.FAT_TOTAL_G] = 0.0
            _nutrientsConsumed[Constants.PROTEIN_G] = 0.0
        }
    }


    private fun getTotalCaloriesConsumed(foodItemsConsumed: List<FoodItemEntity>?) {
        var totalCaloriesConsumed = 0
        if (foodItemsConsumed != null) {
            for (item in foodItemsConsumed) {
                totalCaloriesConsumed += item.servings * (item.foodItemDetails?.calories?.toInt()
                    ?: 0)
            }

            _caloriesConsumed.value = totalCaloriesConsumed.toString()
        } else {
            _caloriesConsumed.value = "0"
        }
    }

    fun removeFoodItem(foodItem: FoodItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.removeFoodItem(foodItem = foodItem)
        }
    }

    fun getFoodItemById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.getFoodItemById(id = id).collect { response ->
                withContext(Dispatchers.Main) {
                    _foodItemEntity.value = response
                }
            }
        }
    }

    fun updateSelectedDay(date: Int, month: String) {
        _selectedDayPair.value = Pair(month, date)
    }

    fun getSavedFoodItems() {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.getAllFoodItems(isConsumed = false).collect {
                _savedFoodItems.value = it
            }
        }
    }

    fun updateSelectedFoodItem(foodItem: FoodItem?) {
        _selectedFoodItem.value = foodItem
    }
}
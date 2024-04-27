package com.sparshchadha.workout_app.features.food.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.domain.entities.WaterEntity
import com.sparshchadha.workout_app.features.food.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.features.food.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.features.food.domain.repository.WaterRepository
import com.sparshchadha.workout_app.features.news.domain.repository.NewsRepository
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


@HiltViewModel
class FoodAndWaterViewModel @Inject constructor(
    private val foodItemsRepository: FoodItemsRepository,
    private val pexelsRepository: PexelsRepository,
    private val waterRepository: WaterRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _foodItemsFromApi = mutableStateOf<Resource<NutritionalValueDto>?>(null)
    val foodItemsFromApi = _foodItemsFromApi

    private val _searchQuery = mutableStateOf("")

    private val _consumedFoodItems = MutableStateFlow<Resource<List<FoodItemEntity>>?>(null)
    val consumedFoodItems = _consumedFoodItems

    private val _caloriesConsumed: MutableState<String?> = mutableStateOf(null)
    val caloriesConsumed: State<String?> = _caloriesConsumed

    private val _nutrientsConsumed: MutableMap<String, Double> = mutableStateMapOf()
    val nutrientsConsumed: Map<String, Double> = _nutrientsConsumed

    private val _selectedDateAndMonthForFoodItems = MutableStateFlow(HelperFunctions.getCurrentDateAndMonth())
    val selectedDateAndMonthForFoodItems = _selectedDateAndMonthForFoodItems.asStateFlow()

    private val _foodItemEntity = mutableStateOf<FoodItemEntity?>(null)
    val foodItemEntity: State<FoodItemEntity?> = _foodItemEntity

    private val _selectedFoodItem = mutableStateOf<FoodItem?>(null)
    val selectedFoodItem: State<FoodItem?> = _selectedFoodItem

    private val _savedFoodItems = MutableStateFlow<List<FoodItemEntity>?>(null)
    val savedFoodItems = _savedFoodItems.asStateFlow()

    private val _createdItems = MutableStateFlow<List<FoodItemEntity?>?>(null)
    val createdItems = _createdItems.asStateFlow()

    private val _waterGlassesEntity = MutableStateFlow<WaterEntity?>(null)
    val waterGlassesEntity = _waterGlassesEntity.asStateFlow()

    private val _selectedDayPair = mutableStateOf(
        Pair(
            HelperFunctions.getCurrentDateAndMonth().second,
            HelperFunctions.getCurrentDateAndMonth().first
        )
    )
    val selectedDayPosition: State<Pair<String, Int>> = _selectedDayPair

    init {
        getFoodItemsConsumedOn()
    }

    fun getFoodItemsFromApi() {
        viewModelScope.launch {
            try {
                val nutritionalValues =
                    foodItemsRepository.getFoodItemsFromApi(foodSearchQuery = _searchQuery.value)
                nutritionalValues.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _foodItemsFromApi.value = Resource.Success(result.data)
                        }

                        is Resource.Loading -> {
                            _foodItemsFromApi.value = Resource.Loading()
                        }

                        is Resource.Error -> {
                            _foodItemsFromApi.value = Resource.Error(error = result.error!!)
                        }
                    }
                }
            } catch (e: Exception) {
                _foodItemsFromApi.value = Resource.Error(error = e)
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
        _consumedFoodItems.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodItems = foodItemsRepository.getFoodItemsConsumedOn(date, month)
                foodItems.collect { response ->
                    withContext(Dispatchers.Main) {
                        _consumedFoodItems.value = Resource.Success(response)
                        getTotalCaloriesConsumed(foodItemsConsumed = response)
                        getNutrientsConsumed(foodItemsConsumed = response)
                        _selectedDateAndMonthForFoodItems.value = (Pair(date.toInt(), month))
                        _selectedDayPair.value = Pair(month, date.toInt())
                    }
                }
            } catch (e: Exception) {
                _consumedFoodItems.value = Resource.Error(error = e)
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

    private val TAG = "FoodAndWaterViewModel"

    fun getWaterGlassesConsumedOn(
        date: String, month: String, year: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            waterRepository.getGlassesConsumedOn(
                date = date, month = month, year = year
            ).collect {
                Log.d(TAG, "getWaterGlassesConsumedOn: $it")
                _waterGlassesEntity.value = it
            }
        }
    }

    fun updateWaterGlassesEntity(
        waterEntity: WaterEntity
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isUpdatable = waterRepository.doesRowExist(waterEntity.date, waterEntity.month, waterEntity.year)
            if (isUpdatable == 1) {
                waterRepository.updateGlassesConsumed(waterEntity)
            } else {
                waterRepository.insertGlassesConsumed(waterEntity)
            }
        }
    }

    fun getCreatedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            foodItemsRepository.getCreatedItems().collect {
                _createdItems.value = it
            }
        }
    }
}
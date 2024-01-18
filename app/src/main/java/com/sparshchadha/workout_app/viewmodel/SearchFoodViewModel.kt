package com.sparshchadha.workout_app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchFoodViewModellll"
@HiltViewModel
class SearchFoodViewModel @Inject constructor(
    private val foodItemsRepository: FoodItemsRepository
) : ViewModel() {
    private val _foodItems = mutableStateOf<NutritionalValueDto?>(null)
    val foodItems  = _foodItems

    private val _searchQuery = mutableStateOf("")

    private val _showToast = mutableStateOf(false)
    val showToast = _showToast

    fun getFoodItems() {
        viewModelScope.launch {
            val response = foodItemsRepository.getFoodItems(_searchQuery.value)
            response.collect { result ->
                when(result) {
                    is Resource.Success -> {
                        showToast()
                        Log.e(TAG, "getFoodItems: response - ${result.data}", )
                        _foodItems.value = result.data
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun showToast() {
        showToast.value = true
    }

    fun updateSearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
}
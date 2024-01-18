package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface FoodItemsRepository {
    fun getFoodItems(foodSearchQuery: String) : Flow<Resource<NutritionalValueDto>>
}
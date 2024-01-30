package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface FoodItemsRepository {
    fun getFoodItems(foodSearchQuery: String) : Flow<Resource<NutritionalValueDto>>

    suspend fun saveFoodItem(foodItemEntity: FoodItemEntity)

    suspend fun getFoodItemsConsumedToday() : Flow<Resource<List<FoodItemEntity>>>

    suspend fun getAllFoodItemsConsumed() : Flow<Resource<List<FoodItemEntity>>>

}
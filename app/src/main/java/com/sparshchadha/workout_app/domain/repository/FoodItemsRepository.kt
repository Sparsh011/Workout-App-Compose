package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface FoodItemsRepository {
    fun getFoodItemsFromApi(foodSearchQuery: String) : Flow<Resource<NutritionalValueDto>>

    suspend fun saveFoodItem(foodItemEntity: FoodItemEntity)

    suspend fun getFoodItemsConsumedOn(date: String, month: String) : Flow<List<FoodItemEntity>>

    suspend fun getAllFoodItemsConsumed() : Flow<List<FoodItemEntity>>

    suspend fun saveOrUpdateCaloriesGoal(caloriesGoal: String)

    suspend fun getCaloriesGoal() : Flow<String?>

    suspend fun removeFoodItem(foodItem: FoodItemEntity)

    suspend fun getFoodItemById(id: Int) : Flow<FoodItemEntity?>

}
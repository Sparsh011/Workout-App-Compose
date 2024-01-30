package com.sparshchadha.workout_app.data.repository

import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.local.dao.FoodItemsDao
import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.api.FoodApi
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    val api: FoodApi,
    private val foodItemsDao: FoodItemsDao
) : FoodItemsRepository {

    override fun getFoodItems(foodSearchQuery: String): Flow<Resource<NutritionalValueDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteDishes = api.getNutritionalValue(query = foodSearchQuery, apiKey = BuildConfig.FOOD_API_KEY)
            emit(Resource.Success(remoteDishes))

        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override suspend fun saveFoodItem(foodItemEntity: FoodItemEntity) {
        foodItemsDao.addFoodItem(foodItem = foodItemEntity)
    }

    override suspend fun getFoodItemsConsumedOn(date: String, month: String): Flow<Resource<List<FoodItemEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val foodItems = foodItemsDao.getFoodItemsConsumedOn(
                date = date,
                month = month
            )
            emit(Resource.Success(foodItems))

        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override suspend fun getAllFoodItemsConsumed(): Flow<Resource<List<FoodItemEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val foodItems = foodItemsDao.getAllFoodItemsConsumed()
            emit(Resource.Success(foodItems))

        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }
}
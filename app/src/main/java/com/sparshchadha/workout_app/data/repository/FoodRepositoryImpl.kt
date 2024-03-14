package com.sparshchadha.workout_app.data.repository

import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.local.datastore.WorkoutAppDatastorePreference
import com.sparshchadha.workout_app.data.local.room_db.dao.FoodItemsDao
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.api.FoodApi
import com.sparshchadha.workout_app.data.remote.dto.food_api.NutritionalValueDto
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val api: FoodApi,
    private val foodItemsDao: FoodItemsDao,
    private val datastorePreference: WorkoutAppDatastorePreference,
) : FoodItemsRepository {

    override fun getFoodItemsFromApi(foodSearchQuery: String): Flow<Resource<NutritionalValueDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteDishes = api.getNutritionalValue(
                    query = foodSearchQuery,
                    apiKey = BuildConfig.FOOD_API_KEY
                )
                emit(Resource.Success(remoteDishes))

            } catch (e: Exception) {
                emit(Resource.Error(error = e))
            }
        }

    override suspend fun saveFoodItem(foodItemEntity: FoodItemEntity) {
        foodItemsDao.addFoodItem(foodItem = foodItemEntity)
    }

    override suspend fun getFoodItemsConsumedOn(
        date: String,
        month: String
    ): Flow<List<FoodItemEntity>> {
        return foodItemsDao.getFoodItemsConsumedOn(date = date, month = month)
    }

    override suspend fun getAllFoodItems(isConsumed: Boolean): Flow<List<FoodItemEntity>> {
        return if (isConsumed) foodItemsDao.getAllFoodItemsConsumed()
        else foodItemsDao.getSavedFoodItems()
    }

    override suspend fun saveOrUpdateCaloriesGoal(caloriesGoal: String) {
        datastorePreference.saveCaloriesGoal(caloriesGoal)
    }

    override suspend fun getCaloriesGoal(): Flow<String?> {
        return datastorePreference.readCaloriesGoal
    }

    override suspend fun removeFoodItem(foodItem: FoodItemEntity) {
        foodItemsDao.removeFoodItem(foodItem = foodItem)
    }

    override suspend fun getFoodItemById(id: Int): Flow<FoodItemEntity?> {
        return foodItemsDao.getFoodItemById(id)
    }
}
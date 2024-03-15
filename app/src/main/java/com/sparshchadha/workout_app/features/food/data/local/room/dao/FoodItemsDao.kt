package com.sparshchadha.workout_app.features.food.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodItem(foodItem: FoodItemEntity): Long

    @Query("SELECT * FROM FoodItemEntity")
    fun getAllFoodItemsConsumed(): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM FoodItemEntity WHERE date = :date AND month = :month")
    fun getFoodItemsConsumedOn(date: String, month: String): Flow<List<FoodItemEntity>>

    @Delete
    fun removeFoodItem(foodItem: FoodItemEntity)

    @Query("SELECT * FROM FoodItemEntity WHERE id = :itemId")
    fun getFoodItemById(itemId: Int): Flow<FoodItemEntity?>

    @Query("SELECT * FROM FoodItemEntity WHERE servings = -1")
    fun getSavedFoodItems(): Flow<List<FoodItemEntity>>
}
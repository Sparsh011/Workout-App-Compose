package com.sparshchadha.workout_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.data.local.entities.FoodItemEntity

@Dao
interface FoodItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodItem(foodItem: FoodItemEntity): Long

    @Query("SELECT * FROM FoodItemEntity")
    fun getAllFoodItemsConsumed(): List<FoodItemEntity>

    @Query("SELECT * FROM FoodItemEntity WHERE date = :currentDate AND month = :currentMonth")
    fun getFoodItemsConsumedToday(currentDate: String, currentMonth: String): List<FoodItemEntity>
}
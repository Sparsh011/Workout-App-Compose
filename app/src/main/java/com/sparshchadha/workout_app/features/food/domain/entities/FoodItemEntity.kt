package com.sparshchadha.workout_app.features.food.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.FoodItem
import java.time.LocalTime

@Entity
data class FoodItemEntity(
    val date: String,
    val month: String,
    val servings: Int,
    val hour: Int = LocalTime.now().hour,
    val minutes: Int = LocalTime.now().minute,
    val seconds: Int = LocalTime.now().second,
    val foodItemDetails: FoodItem?,
    val isConsumed: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)

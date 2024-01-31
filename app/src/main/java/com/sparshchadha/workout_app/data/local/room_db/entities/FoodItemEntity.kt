package com.sparshchadha.workout_app.data.local.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem

@Entity
data class FoodItemEntity(
    val date: String,
    val month: String,
    val quantity: Int,
    val foodItemDetails: FoodItem?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

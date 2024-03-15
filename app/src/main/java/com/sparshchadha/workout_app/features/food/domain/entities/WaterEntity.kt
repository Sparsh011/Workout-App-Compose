package com.sparshchadha.workout_app.features.food.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterEntity(
    val glassesConsumed: Int,
    val date: String,
    val month: String,
    val year: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
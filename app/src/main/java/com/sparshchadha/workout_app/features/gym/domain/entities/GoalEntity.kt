package com.sparshchadha.workout_app.features.gym.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GoalEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val description: String,
    val deadlineDay: String = "",
    val deadlineMonth: String = "",
    val deadlineYear: String = "",
    val reps: Int? = null,
    val weightUnit: String,
    val targetWeight: Double? = null,
    val priority: String = "Undefined"
)
package com.sparshchadha.workout_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDtoItem

@Entity
data class GymExercisesEntity (
    val date: String,
    val month: String,
    val setsPerformed: Int,
    val exerciseDetails: GymWorkoutsDtoItem?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

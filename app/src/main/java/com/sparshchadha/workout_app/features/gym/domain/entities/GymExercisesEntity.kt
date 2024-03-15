package com.sparshchadha.workout_app.features.gym.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDtoItem
import java.time.LocalTime

@Entity
data class GymExercisesEntity(
    val date: String,
    val month: String,
    val setsPerformed: Int,
    val exerciseDetails: GymExercisesDtoItem?,
    val hour: Int = LocalTime.now().hour,
    val minutes: Int = LocalTime.now().minute,
    val seconds: Int = LocalTime.now().second,
    val isPerformed: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)

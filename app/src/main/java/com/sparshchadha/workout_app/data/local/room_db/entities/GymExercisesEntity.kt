package com.sparshchadha.workout_app.data.local.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDtoItem
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
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)

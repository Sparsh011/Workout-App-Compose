package com.sparshchadha.workout_app.data.remote.dto.gym_workout

data class GymExercisesDtoItem(
    val difficulty: String,
    val equipment: String,
    val instructions: String,
    val muscle: String,
    val name: String,
    val type: String,
)
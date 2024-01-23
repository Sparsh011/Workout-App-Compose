package com.sparshchadha.workout_app.util

import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.ui.screens.workout.gym.MuscleType
import com.sparshchadha.workout_app.ui.screens.workout.gym.WorkoutType
import com.sparshchadha.workout_app.util.Extensions.capitalize

object HelperFunctions {
    fun getDifficultyLevels() : List<String> {
        return listOf(
            DifficultyLevel.BEGINNER.name.lowercase().capitalize(),
            DifficultyLevel.INTERMEDIATE.name.lowercase().capitalize(),
            DifficultyLevel.EXPERT.name.lowercase().capitalize()
        )
    }

    fun getMuscleTypes() : List<String> {
        return listOf(
            MuscleType.ABDOMINALS.name.lowercase().capitalize(),
            MuscleType.ABDUCTORS.name.lowercase().capitalize(),
            MuscleType.ADDUCTORS.name.lowercase().capitalize(),
            MuscleType.BICEPS.name.lowercase().capitalize(),
            MuscleType.CALVES.name.lowercase().capitalize(),
            MuscleType.CHEST.name.lowercase().capitalize(),
            MuscleType.FOREARMS.name.lowercase().capitalize(),
            MuscleType.GLUTES.name.lowercase().capitalize(), MuscleType.HAMSTRINGS.name.lowercase().capitalize(),
            MuscleType.LATS.name.lowercase().capitalize(),
            MuscleType.LOWER_BACK.name.lowercase().capitalize().replace('_', ' '),
            MuscleType.MIDDLE_BACK.name.lowercase().capitalize().replace('_', ' '),
            MuscleType.NECK.name.lowercase().capitalize(),
            MuscleType.QUADRICEPS.name.lowercase().capitalize(),
            MuscleType.TRAPS.name.lowercase().capitalize(),
            MuscleType.TRICEPS.name.lowercase().capitalize(),
        )
    }

    fun getWorkoutTypes() : List<String> {
        return listOf(
            WorkoutType.CARDIO.name.lowercase().capitalize(),
            WorkoutType.OLYMPIC_WEIGHTLIFTING.name.lowercase().capitalize().replace('_', ' '),
            WorkoutType.PLYOMETRICS.name.lowercase().capitalize(),
            WorkoutType.POWERLIFTING.name.lowercase().capitalize(),
            WorkoutType.STRENGTH.name.lowercase().capitalize(),
            WorkoutType.STRETCHING.name.lowercase().capitalize(),
            WorkoutType.STRONGMAN.name.lowercase().capitalize(),
        )
    }
}
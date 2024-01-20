package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getYogaPosesByDifficulty(difficulty: DifficultyLevel) : Flow<Resource<YogaPosesDto>>

    fun getExercisesByDifficultyLevel(difficulty: String) : Flow<Resource<GymWorkoutsDto>>

    fun getExercisesByMuscle(muscleType: String) : Flow<Resource<GymWorkoutsDto>>

    fun getExercisesByWorkoutType(workoutType: String) : Flow<Resource<GymWorkoutsDto>>

    fun getExerciseByName(name: String) : Flow<Resource<GymWorkoutsDto>>
}
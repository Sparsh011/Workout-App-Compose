package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getYogaPosesByDifficulty(difficulty: DifficultyLevel) : Flow<Resource<YogaPosesDto>>

    fun getExercisesByDifficultyLevel(difficulty: String) : Flow<Resource<GymExercisesDto>>

    fun getExercisesByMuscle(muscleType: String) : Flow<Resource<GymExercisesDto>>

    fun getExercisesByWorkoutType(workoutType: String) : Flow<Resource<GymExercisesDto>>

    fun getExerciseByName(name: String) : Flow<Resource<GymExercisesDto>>

    suspend fun getAllYogaPosesPerformed() : Flow<Resource<List<YogaEntity>>>

    suspend fun saveYogaPose(yogaPose: YogaEntity)

    suspend fun getYogaPosesPerformedToday() : Flow<Resource<List<YogaEntity>>>

    suspend fun saveGymExercise(gymExercisesEntity: GymExercisesEntity)

    suspend fun getGymExercisesPerformedToday() : Flow<Resource<List<GymExercisesEntity>>>

    suspend fun getAllGymExercisesPerformed() : Flow<Resource<List<GymExercisesEntity>>>

}
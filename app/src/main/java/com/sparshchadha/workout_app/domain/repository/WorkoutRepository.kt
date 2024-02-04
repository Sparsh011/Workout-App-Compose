package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getYogaPosesByDifficultyFromApi(difficulty: DifficultyLevel) : Flow<Resource<YogaPosesDto>>

    fun getExercisesByDifficultyLevelFromApi(difficulty: String) : Flow<Resource<GymExercisesDto>>

    fun getExercisesByMuscleFromApi(muscleType: String) : Flow<Resource<GymExercisesDto>>

    fun getExercisesByWorkoutTypeFromApi(workoutType: String) : Flow<Resource<GymExercisesDto>>

    fun getExerciseByNameFromApi(name: String) : Flow<Resource<GymExercisesDto>>

    suspend fun getAllYogaPosesPerformed() : Flow<List<YogaEntity>>

    suspend fun saveYogaPose(yogaPose: YogaEntity)

    suspend fun getYogaPosesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second
    ): Flow<List<YogaEntity>>

    suspend fun saveGymExercise(gymExercisesEntity: GymExercisesEntity)

    suspend fun getGymExercisesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second
    ): Flow<List<GymExercisesEntity>>

    suspend fun getAllGymExercisesPerformed() : Flow<List<GymExercisesEntity>>

}
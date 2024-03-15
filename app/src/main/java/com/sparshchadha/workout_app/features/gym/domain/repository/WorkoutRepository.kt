package com.sparshchadha.workout_app.features.gym.domain.repository

import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.YogaPosesDto
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getYogaPosesByDifficultyFromApi(difficulty: DifficultyLevel): Flow<Resource<YogaPosesDto>>

    fun getExercisesByDifficultyLevelFromApi(difficulty: String): Flow<Resource<GymExercisesDto>>

    fun getExercisesByMuscleFromApi(muscleType: String): Flow<Resource<GymExercisesDto>>

    fun getExercisesByWorkoutTypeFromApi(workoutType: String): Flow<Resource<GymExercisesDto>>

    fun getExerciseByNameFromApi(name: String): Flow<Resource<GymExercisesDto>>

    suspend fun getAllPoses(performed: Boolean): Flow<List<YogaEntity>>

    suspend fun removeYogaPose(yogaPose: YogaEntity)

    suspend fun getYogaPosesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ): Flow<List<YogaEntity>>

    suspend fun getGymExercisesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ): Flow<List<GymExercisesEntity>>


    suspend fun getAllYogaPosesPerformed(): Flow<List<YogaEntity>>

    suspend fun getAllGymExercisesPerformed(): Flow<List<GymExercisesEntity>>

    suspend fun getAllExercises(performed: Boolean): Flow<List<GymExercisesEntity>>

    suspend fun removeGymExercise(exercisesEntity: GymExercisesEntity)

    suspend fun saveExerciseToDB(exercisesEntity: GymExercisesEntity)

    suspend fun saveYogaPoseToDB(yogaPose: YogaEntity)

}
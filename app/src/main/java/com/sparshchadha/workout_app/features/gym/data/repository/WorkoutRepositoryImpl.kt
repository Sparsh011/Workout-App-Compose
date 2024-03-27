package com.sparshchadha.workout_app.features.gym.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.features.gym.data.local.room.dao.GymExercisesDao
import com.sparshchadha.workout_app.features.gym.data.remote.api.GymExercisesApi
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class WorkoutRepositoryImpl(
    val gymExercisesApi: GymExercisesApi,
    val gymExercisesDao: GymExercisesDao,
) : WorkoutRepository {

    override fun getExercisesByDifficultyLevelFromApi(difficulty: String): Flow<Resource<GymExercisesDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteExercises = gymExercisesApi.getExercisesByDifficultyLevel(
                    difficulty = difficulty.lowercase(),
                    apiKey = BuildConfig.GYM_WORKOUTS_API_KEY
                )
                if (remoteExercises.isSuccessful) {
                    emit(Resource.Success(remoteExercises.body()))
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(error = e)
                )
            }
        }

    override fun getExercisesByMuscleFromApi(muscleType: String): Flow<Resource<GymExercisesDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteExercises = gymExercisesApi.getExercisesByMuscle(
                    muscle = muscleType.lowercase(),
                    apiKey = BuildConfig.GYM_WORKOUTS_API_KEY
                )
                if (remoteExercises.isSuccessful) {
                    emit(Resource.Success(remoteExercises.body()))
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(error = e)
                )
            }
        }

    override fun getExercisesByWorkoutTypeFromApi(workoutType: String): Flow<Resource<GymExercisesDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteExercises = gymExercisesApi.getExercisesByWorkoutType(
                    workoutType = workoutType.lowercase(),
                    apiKey = BuildConfig.GYM_WORKOUTS_API_KEY
                )
                if (remoteExercises.isSuccessful) {
                    emit(Resource.Success(remoteExercises.body()))
                }
            } catch (e: Exception) {
                emit(
                    Resource.Error(error = e)
                )
            }
        }

    override fun getExerciseByNameFromApi(name: String): Flow<Resource<GymExercisesDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteExercises = gymExercisesApi.getExerciseByName(
                name = name.lowercase(),
                apiKey = BuildConfig.GYM_WORKOUTS_API_KEY
            )
            if (remoteExercises.isSuccessful) {
                emit(Resource.Success(remoteExercises.body()))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override suspend fun getGymExercisesPerformedOn(
        date: String,
        month: String
    ): Flow<List<GymExercisesEntity>> {
        return gymExercisesDao.getExercisesPerformedOn(
            date = date,
            month = month
        )
    }

    override suspend fun getAllExercises(performed: Boolean): Flow<List<GymExercisesEntity>> {
        return if (performed) gymExercisesDao.getAllExercisesPerformed()
        else gymExercisesDao.getSavedExercises()
    }

    override suspend fun removeGymExercise(exercisesEntity: GymExercisesEntity) {
        gymExercisesDao.removeGymExercise(exercisesEntity)
    }

    override suspend fun saveExerciseToDB(exercisesEntity: GymExercisesEntity) {
        gymExercisesDao.addGymExercise(exercisesEntity)
    }

    override suspend fun getAllGymExercisesPerformed(): Flow<List<GymExercisesEntity>> {
        return gymExercisesDao.getAllExercisesPerformed()
    }
}
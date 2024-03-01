package com.sparshchadha.workout_app.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.local.room_db.dao.GymExercisesDao
import com.sparshchadha.workout_app.data.local.room_db.dao.YogaDao
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.api.GymExercisesApi
import com.sparshchadha.workout_app.data.remote.api.YogaApi
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class WorkoutRepositoryImpl(
    val yogaApi: YogaApi,
    val gymExercisesApi: GymExercisesApi,
    val yogaDao: YogaDao,
    val gymExercisesDao: GymExercisesDao,
) : WorkoutRepository {

    override fun getYogaPosesByDifficultyFromApi(difficulty: DifficultyLevel): Flow<Resource<YogaPosesDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteYogaPoses =
                    yogaApi.getYogaPosesByDifficulty(difficulty = difficulty.name.lowercase())
                emit(Resource.Success(remoteYogaPoses))
            } catch (e: Exception) {
                emit(
                    Resource.Error(error = e)
                )
            }
        }

    override fun getExercisesByDifficultyLevelFromApi(difficulty: String): Flow<Resource<GymExercisesDto>> =
        flow {
            emit(Resource.Loading())

            try {
                val remoteExercises = gymExercisesApi.getExercisesByDifficultyLevel(
                    difficulty = difficulty.lowercase(),
                    apiKey = BuildConfig.GYM_WORKOUTS_API_KEY
                )
                emit(Resource.Success(remoteExercises))
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
                emit(Resource.Success(remoteExercises))
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
                emit(Resource.Success(remoteExercises))
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
            emit(Resource.Success(remoteExercises))
        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override suspend fun getAllYogaPosesPerformed(): Flow<List<YogaEntity>> {
        return yogaDao.getAllPerformedYogaPoses()
    }

    override suspend fun saveYogaPose(yogaPose: YogaEntity) {
        yogaDao.addYogaPose(yogaPose = yogaPose)
    }

    override suspend fun getYogaPosesPerformedOn(
        date: String,
        month: String
    ): Flow<List<YogaEntity>> {
        return yogaDao.getYogaPosesPerformedOn(
            date = date,
            month = month
        )
    }

    override suspend fun saveGymExercise(gymExercisesEntity: GymExercisesEntity) {
        gymExercisesDao.addGymExercise(gymExercisesEntity = gymExercisesEntity)
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

    override suspend fun getAllGymExercisesPerformed(): Flow<List<GymExercisesEntity>> {
        return gymExercisesDao.getAllExercisesPerformed()
    }
}
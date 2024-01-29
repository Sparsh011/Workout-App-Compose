package com.sparshchadha.workout_app.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.local.dao.YogaDao
import com.sparshchadha.workout_app.data.local.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.api.GymExercisesApi
import com.sparshchadha.workout_app.data.remote.api.YogaApi
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class WorkoutRepositoryImpl (
    val yogaApi: YogaApi,
    val gymExercisesApi: GymExercisesApi,
    val yogaDao: YogaDao
) : WorkoutRepository {

    override fun getYogaPosesByDifficulty(difficulty: DifficultyLevel): Flow<Resource<YogaPosesDto>> = flow {
        emit(Resource.Loading())

        try {
            val remoteYogaPoses = yogaApi.getYogaPosesByDifficulty(difficulty = difficulty.name.lowercase())
            emit(Resource.Success(remoteYogaPoses))
        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override fun getExercisesByDifficultyLevel(difficulty: String): Flow<Resource<GymExercisesDto>> = flow {
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

    override fun getExercisesByMuscle(muscleType: String): Flow<Resource<GymExercisesDto>> = flow {
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

    override fun getExercisesByWorkoutType(workoutType: String): Flow<Resource<GymExercisesDto>> = flow {
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

    override fun getExerciseByName(name: String): Flow<Resource<GymExercisesDto>> = flow {
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

    override suspend fun getSavedYogaPoses(): Flow<Resource<List<YogaEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val poses = yogaDao.getAllPerformedYogaPoses()
            emit(Resource.Success(poses))
        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }

    override suspend fun saveYogaPose(yogaPose: YogaEntity) {
        yogaDao.addYogaPose(yogaPose = yogaPose)
    }

    override suspend fun getYogaPosesPerformedToday(): Flow<Resource<List<YogaEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val poses = yogaDao.getYogaPosesPerformedToday(
                currentDate = HelperFunctions.getCurrentDateAndMonth().first.toString(),
                currentMonth = HelperFunctions.getCurrentDateAndMonth().second
            )
            emit(Resource.Success(poses))
        } catch (e: Exception) {
            emit(
                Resource.Error(error = e)
            )
        }
    }
}
package com.sparshchadha.workout_app.features.yoga.data.repository

import com.sparshchadha.workout_app.features.yoga.data.local.room.dao.YogaDao
import com.sparshchadha.workout_app.features.yoga.data.remote.api.YogaApi
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.YogaPosesDto
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.features.yoga.domain.repository.YogaRepository
import com.sparshchadha.workout_app.shared_ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class YogaRepositoryImpl(
    private val yogaDao: YogaDao,
    private val yogaApi: YogaApi,
) : YogaRepository {

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


    override suspend fun getAllPoses(performed: Boolean): Flow<List<YogaEntity>> {
        return if (performed) yogaDao.getAllPerformedYogaPoses()
        else yogaDao.getSavedPoses()
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

    override suspend fun removeYogaPose(yogaPose: YogaEntity) {
        yogaDao.removeYogaPose(yogaPose)
    }

    override suspend fun saveYogaPoseToDB(yogaPose: YogaEntity) {
        yogaDao.addYogaPose(yogaPose)
    }

}
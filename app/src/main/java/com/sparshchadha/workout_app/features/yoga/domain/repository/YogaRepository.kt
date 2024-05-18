package com.sparshchadha.workout_app.features.yoga.domain.repository

import com.sparshchadha.workout_app.features.yoga.data.remote.dto.YogaPosesDto
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface YogaRepository {
    fun getYogaPosesByDifficultyFromApi(difficulty: DifficultyLevel): Flow<Resource<YogaPosesDto>>

    suspend fun getAllPoses(performed: Boolean): Flow<List<YogaEntity>>

    suspend fun removeYogaPose(yogaPose: YogaEntity)

    suspend fun getYogaPosesPerformedOn(
        date: String = HelperFunctions.getCurrentDateAndMonth().first.toString(),
        month: String = HelperFunctions.getCurrentDateAndMonth().second,
    ): Flow<List<YogaEntity>>

    suspend fun saveYogaPoseToDB(yogaPose: YogaEntity)
}
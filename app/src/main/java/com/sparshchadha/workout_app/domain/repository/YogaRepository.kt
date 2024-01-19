package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaDifficultyLevels
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface YogaRepository {
    fun getYogaPosesByDifficulty(difficulty: YogaDifficultyLevels) : Flow<Resource<YogaPosesDto>>
}
package com.sparshchadha.workout_app.features.food.domain.repository

import com.sparshchadha.workout_app.features.food.data.remote.dto.pexels.PexelResponseDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface PexelsRepository {
    fun searchImageOnPexel(searchQuery: String): Flow<Resource<PexelResponseDto>>
}
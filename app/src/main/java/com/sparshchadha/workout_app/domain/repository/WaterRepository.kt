package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.WaterEntity
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    suspend fun getGlassesConsumedOn(date: String, month: String, year: String): Flow<WaterEntity>
    suspend fun updateGlassesConsumed(waterEntity: WaterEntity): Int
    suspend fun insertGlassesConsumed(waterEntity: WaterEntity)
    suspend fun doesRowExist(id: Int): Boolean
}
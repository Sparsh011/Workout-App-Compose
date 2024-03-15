package com.sparshchadha.workout_app.features.food.data.repository

import com.sparshchadha.workout_app.features.food.data.local.room.dao.WaterDao
import com.sparshchadha.workout_app.features.food.domain.entities.WaterEntity
import com.sparshchadha.workout_app.features.food.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow

class WaterRepositoryImpl(
    val waterDao: WaterDao
) : WaterRepository {
    override suspend fun getGlassesConsumedOn(
        date: String,
        month: String,
        year: String
    ): Flow<WaterEntity> {
        return waterDao.getGlassesConsumedOn(date = date, month = month, year = year)
    }

    override suspend fun updateGlassesConsumed(waterEntity: WaterEntity): Int {
        return waterDao.updateGlassesConsumed(waterEntity)
    }

    override suspend fun insertGlassesConsumed(waterEntity: WaterEntity) {
        waterDao.insertGlassesConsumed(waterEntity)
    }

    override suspend fun doesRowExist(id: Int): Boolean {
        return waterDao.doesRowExist(id)
    }
}
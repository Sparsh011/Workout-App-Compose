package com.sparshchadha.workout_app.features.gym.data.repository

import com.sparshchadha.workout_app.features.gym.data.local.room.dao.PRDao
import com.sparshchadha.workout_app.features.gym.domain.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.features.gym.domain.repository.PRRepository
import kotlinx.coroutines.flow.Flow

class PRRepositoryImpl(
    val prDao: PRDao
) : PRRepository {
    override suspend fun addPR(pr: PersonalRecordsEntity) {
        prDao.addPR(pr)
    }

    override suspend fun updatePR(pr: PersonalRecordsEntity) {
        prDao.updatePR(pr)
    }

    override suspend fun deletePR(pr: PersonalRecordsEntity) {
        prDao.deletePR(pr)
    }

    override suspend fun getAllPR(category: String): Flow<List<PersonalRecordsEntity>> {
        return prDao.getAllPR(forCategory = category)
    }
}
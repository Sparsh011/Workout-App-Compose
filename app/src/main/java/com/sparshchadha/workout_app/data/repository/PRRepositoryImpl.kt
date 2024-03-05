package com.sparshchadha.workout_app.data.repository

import com.sparshchadha.workout_app.data.local.room_db.dao.PRDao
import com.sparshchadha.workout_app.data.local.room_db.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.domain.repository.PRRepository
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
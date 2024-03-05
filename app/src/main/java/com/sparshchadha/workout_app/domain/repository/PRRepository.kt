package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.PersonalRecordsEntity
import kotlinx.coroutines.flow.Flow

interface PRRepository {
    suspend fun addPR(pr: PersonalRecordsEntity)

    suspend fun updatePR(pr: PersonalRecordsEntity)

    suspend fun deletePR(pr: PersonalRecordsEntity)

    suspend fun getAllPR(category: String): Flow<List<PersonalRecordsEntity>>
}
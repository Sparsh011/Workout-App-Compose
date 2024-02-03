package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface RemindersRepository {
    suspend fun addReminder(reminderEntity: ReminderEntity) : Long

    suspend fun updateReminder(reminderEntity: ReminderEntity) : Int

    suspend fun getRemindersByReminderType(reminderType: String) : Flow<List<ReminderEntity>>
}
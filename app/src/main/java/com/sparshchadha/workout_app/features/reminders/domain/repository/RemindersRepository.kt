package com.sparshchadha.workout_app.features.reminders.domain.repository

import com.sparshchadha.workout_app.features.reminders.domain.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface RemindersRepository {
    suspend fun addReminder(reminderEntity: ReminderEntity): Long

    suspend fun updateReminder(reminderEntity: ReminderEntity): Int

    suspend fun getRemindersByReminderType(reminderType: String): Flow<List<ReminderEntity>>

    suspend fun deleteReminder(reminderEntity: ReminderEntity)

    suspend fun getAllReminders(): List<ReminderEntity>
}
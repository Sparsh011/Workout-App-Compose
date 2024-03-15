package com.sparshchadha.workout_app.data.repository

import com.sparshchadha.workout_app.data.local.room_db.dao.RemindersDao
import com.sparshchadha.workout_app.data.local.room_db.entities.ReminderEntity
import com.sparshchadha.workout_app.domain.repository.RemindersRepository
import kotlinx.coroutines.flow.Flow

class RemindersRepositoryImpl(
    private val remindersDao: RemindersDao,
) : RemindersRepository {

    override suspend fun addReminder(reminderEntity: ReminderEntity): Long {
        return remindersDao.addReminder(reminderEntity = reminderEntity)
    }

    override suspend fun updateReminder(reminderEntity: ReminderEntity): Int {
        return remindersDao.updateReminder(reminderEntity = reminderEntity)
    }

    override suspend fun getRemindersByReminderType(reminderType: String): Flow<List<ReminderEntity>> {
        return remindersDao.getRemindersByReminderType(reminderType = reminderType)
    }

    override suspend fun deleteReminder(reminderEntity: ReminderEntity) {
        remindersDao.deleteReminder(reminderEntity = reminderEntity)
    }

    override suspend fun getAllReminders(): List<ReminderEntity> {
        return remindersDao.getAllReminders()
    }
}
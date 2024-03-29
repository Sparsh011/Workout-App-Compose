package com.sparshchadha.workout_app.features.reminders.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sparshchadha.workout_app.features.reminders.domain.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RemindersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminderEntity: ReminderEntity): Long

    @Query("SELECT * FROM ReminderEntity WHERE reminderType = :reminderType")
    fun getRemindersByReminderType(reminderType: String): Flow<List<ReminderEntity>>

    @Update
    suspend fun updateReminder(reminderEntity: ReminderEntity): Int

    @Delete
    fun deleteReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM ReminderEntity")
    fun getAllReminders(): List<ReminderEntity>
}
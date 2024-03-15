package com.sparshchadha.workout_app.features.reminders.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderEntity(
    val date: String,
    val month: String,
    val year: String,
    val hours: Int,
    val minutes: Int,
    val reminderType: String,
    val reminderDescription: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
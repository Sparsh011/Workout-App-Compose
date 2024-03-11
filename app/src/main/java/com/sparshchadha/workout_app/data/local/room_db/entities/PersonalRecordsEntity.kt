package com.sparshchadha.workout_app.data.local.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalRecordsEntity(
    val category: String = "gym",
    val exerciseName: String,
    val reps: Int,
    val optionalDescription: String = "",
    val date: String,
    val month: String,
    val year: String,
    val weight: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)
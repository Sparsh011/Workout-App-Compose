package com.sparshchadha.workout_app.data.local.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.data.remote.dto.yoga.Pose

@Entity
data class YogaEntity(
    val date: String,
    val month: String,
    val setsPerformed: Int,
    val yogaPoseDetails: Pose?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

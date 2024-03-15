package com.sparshchadha.workout_app.features.yoga.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.Pose
import java.time.LocalTime

@Entity
data class YogaEntity(
    val date: String,
    val month: String,
    val setsPerformed: Int,
    val yogaPoseDetails: Pose?,
    val hour: Int = LocalTime.now().hour,
    val minutes: Int = LocalTime.now().minute,
    val seconds: Int = LocalTime.now().second,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)

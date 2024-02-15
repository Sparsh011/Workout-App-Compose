package com.sparshchadha.workout_app.alarm_manager

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val description: String
)
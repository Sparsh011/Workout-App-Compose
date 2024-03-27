package com.sparshchadha.workout_app.features.yoga.data.remote.dto

data class YogaPosesDto(
    val difficulty_level: String,
    val id: Int,
    val poses: List<Pose>,
)
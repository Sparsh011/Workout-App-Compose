package com.sparshchadha.workout_app.data.remote.dto.yoga

data class YogaPosesDto(
    val difficulty_level: String,
    val id: Int,
    val poses: List<Pose>,
)
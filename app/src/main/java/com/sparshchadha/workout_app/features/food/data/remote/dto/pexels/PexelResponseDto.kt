package com.sparshchadha.workout_app.features.food.data.remote.dto.pexels

data class PexelResponseDto(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int,
)
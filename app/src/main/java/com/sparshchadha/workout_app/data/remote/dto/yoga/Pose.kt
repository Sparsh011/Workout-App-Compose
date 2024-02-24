package com.sparshchadha.workout_app.data.remote.dto.yoga

data class Pose(
    val difficulty_level: String,
    val english_name: String,
    val id: Int,
    val pose_benefits: String,
    val pose_description: String,
    val sanskrit_name: String,
    val sanskrit_name_adapted: String,
    val translation_name: String,
    val url_png: String,
    val url_svg: String,
    val url_svg_alt: String,
)
package com.sparshchadha.workout_app.features.news.data.remote.dto

data class NewsArticlesDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
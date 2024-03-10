package com.sparshchadha.workout_app.data.remote.dto.news_api

data class NewsArticlesDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
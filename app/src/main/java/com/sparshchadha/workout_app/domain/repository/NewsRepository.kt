package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.remote.dto.news_api.NewsArticlesDto

interface NewsRepository {
    suspend fun getNewsArticlesFor(searchQuery: String): NewsArticlesDto
}
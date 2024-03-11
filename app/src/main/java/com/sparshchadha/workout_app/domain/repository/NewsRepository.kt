package com.sparshchadha.workout_app.domain.repository

import com.sparshchadha.workout_app.data.remote.dto.news_api.NewsArticlesDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNewsArticlesFor(searchQuery: String): Flow<Resource<NewsArticlesDto>>
}
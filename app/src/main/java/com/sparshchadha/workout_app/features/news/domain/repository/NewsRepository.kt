package com.sparshchadha.workout_app.features.news.domain.repository

import com.sparshchadha.workout_app.features.news.data.remote.dto.NewsArticlesDto
import com.sparshchadha.workout_app.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNewsArticlesFor(searchQuery: String): Flow<Resource<NewsArticlesDto>>
}
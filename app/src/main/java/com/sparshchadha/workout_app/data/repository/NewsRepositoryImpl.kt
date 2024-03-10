package com.sparshchadha.workout_app.data.repository

import com.sparshchadha.workout_app.BuildConfig
import com.sparshchadha.workout_app.data.remote.api.NewsApi
import com.sparshchadha.workout_app.data.remote.dto.news_api.NewsArticlesDto
import com.sparshchadha.workout_app.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val newsApi: NewsApi
): NewsRepository {
    override suspend fun getNewsArticlesFor(searchQuery: String): NewsArticlesDto {
        return newsApi.getNewsFor(
            searchQuery = searchQuery,
            apiKey = BuildConfig.NEWS_API_KEY
        )
    }
}
package com.sparshchadha.workout_app.features.news.data.remote.api

import com.sparshchadha.workout_app.features.news.data.remote.dto.NewsArticlesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun getNewsFor(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsArticlesDto>

    companion object {
        val BASE_URL = "https://newsapi.org/"
    }
}
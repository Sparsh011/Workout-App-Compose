package com.sparshchadha.workout_app.data.remote.api

import com.sparshchadha.workout_app.data.remote.dto.news_api.NewsArticlesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun getNewsFor(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String
    ): NewsArticlesDto

    companion object {
        val BASE_URL = "https://newsapi.org/"
    }
}
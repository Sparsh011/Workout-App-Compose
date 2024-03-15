package com.sparshchadha.workout_app.features.food.data.remote.api

import com.sparshchadha.workout_app.features.food.data.remote.dto.pexels.PexelResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {
    @GET("v1/search")
    suspend fun searchImageOnPexel(
        @Query("query") searchQuery: String,
        @Header("Authorization") apiKey: String,
    ): PexelResponseDto

    companion object {
        val BASE_URL = "https://api.pexels.com/"
    }
}
package com.sparshchadha.workout_app.data.remote.api

import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface YogaApi {
    @GET("v1/poses")
    suspend fun getYogaPosesByDifficulty(
        @Query("level") difficulty: String,
    ): YogaPosesDto

    companion object {
        val BASE_URL = "https://yoga-api-nzy4.onrender.com/"
    }
}
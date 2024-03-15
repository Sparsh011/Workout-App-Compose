package com.sparshchadha.workout_app.features.food.data.remote.api

import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.NutritionalValueDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoodApi {
    @GET("v1/nutrition")
    suspend fun getNutritionalValue(
        @Query("query") query: String,
        @Header("X-Api-Key") apiKey: String,
    ): NutritionalValueDto

    companion object {
        val BASE_URL = "https://api.calorieninjas.com/"
    }
}
package com.sparshchadha.workout_app.data.remote.api

import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GymExercisesApi {
    @GET("v1/exercises")
    suspend fun getExercisesByDifficultyLevel(
        @Query("difficulty") difficulty: String,
        @Header("X-Api-Key") apiKey: String,
    ): Response<GymExercisesDto>

    @GET("v1/exercises")
    suspend fun getExercisesByMuscle(
        @Query("muscle") muscle: String,
        @Header("X-Api-Key") apiKey: String,
    ): Response<GymExercisesDto>

    @GET("v1/exercises")
    suspend fun getExercisesByWorkoutType(
        @Query("type") workoutType: String,
        @Header("X-Api-Key") apiKey: String,
    ): Response<GymExercisesDto>

    @GET("v1/exercises")
    suspend fun getExerciseByName(
        @Query("name") name: String,
        @Header("X-Api-Key") apiKey: String,
    ): Response<GymExercisesDto>

    companion object {
        val BASE_URL = "https://api.api-ninjas.com/"
    }
}
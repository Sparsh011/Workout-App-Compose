package com.sparshchadha.workout_app.features.google_auth

import retrofit2.http.GET
import retrofit2.http.Query

interface AuthApi {
    @GET("/verify-google-auth-token")
    fun verifyGoogleIdToken(
        @Query("token") idToken: String
    )

    companion object {
        val BASE_URL = "https://shared-server-sparsh-fastapi.onrender.com"
    }
}
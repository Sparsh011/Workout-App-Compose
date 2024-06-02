package com.sparshchadha.workout_app.application

import com.sparshchadha.workout_app.features.google_auth.AuthApi
import com.sparshchadha.workout_app.storage.room_db.WorkoutAppDatabase

class BaseRepository(
    private val workoutAppDatabase: WorkoutAppDatabase,
    private val authApi: AuthApi
) {
    fun clearWorkoutDatabase() {
        workoutAppDatabase.clearAllTables()
    }

    suspend fun sendGoogleIdTokenToBackend(idToken: String) {
        authApi.verifyGoogleIdToken(idToken)
    }
}
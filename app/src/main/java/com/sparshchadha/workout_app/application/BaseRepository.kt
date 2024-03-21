package com.sparshchadha.workout_app.application

import com.sparshchadha.workout_app.storage.room_db.WorkoutAppDatabase

class BaseRepository(
    private val workoutAppDatabase: WorkoutAppDatabase
) {
    fun clearWorkoutDatabase() {
        workoutAppDatabase.clearAllTables()
    }
}
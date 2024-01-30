package com.sparshchadha.workout_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.data.local.entities.GymExercisesEntity

@Dao
interface GymExercisesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGymExercise(gymExercisesEntity: GymExercisesEntity): Long

    @Query("SELECT * FROM GymExercisesEntity")
    fun getAllExercisesPerformed(): List<GymExercisesEntity>

    @Query("SELECT * FROM GymExercisesEntity WHERE date = :currentDate AND month = :currentMonth")
    fun getExercisesPerformedToday(currentDate: String, currentMonth: String): List<GymExercisesEntity>
}
package com.sparshchadha.workout_app.data.local.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GymExercisesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGymExercise(gymExercisesEntity: GymExercisesEntity): Long

    @Query("SELECT * FROM GymExercisesEntity")
    fun getAllExercisesPerformed(): Flow<List<GymExercisesEntity>>

    @Query("SELECT * FROM GymExercisesEntity WHERE date = :date AND month = :month")
    fun getExercisesPerformedOn(date: String, month: String): Flow<List<GymExercisesEntity>>
}
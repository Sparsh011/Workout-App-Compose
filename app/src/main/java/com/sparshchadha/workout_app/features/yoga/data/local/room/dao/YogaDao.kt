package com.sparshchadha.workout_app.features.yoga.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YogaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addYogaPose(yogaPose: YogaEntity): Long

    @Query("SELECT * FROM YogaEntity WHERE setsPerformed != -1")
    fun getAllPerformedYogaPoses(): Flow<List<YogaEntity>>

    @Query("SELECT * FROM YogaEntity WHERE date = :date AND month = :month")
    fun getYogaPosesPerformedOn(date: String, month: String): Flow<List<YogaEntity>>

    @Delete
    fun removeYogaPose(yogaPose: YogaEntity)

    @Query("SELECT * FROM YogaEntity WHERE setsPerformed = -1")
    fun getSavedPoses(): Flow<List<YogaEntity>>
}
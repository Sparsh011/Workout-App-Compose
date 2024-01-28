package com.sparshchadha.workout_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparshchadha.workout_app.data.local.entities.YogaEntity

@Dao
interface YogaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addYogaPose(yogaPose: YogaEntity): Long

    @Query("SELECT * FROM yogaentity ORDER BY id ASC")
    fun getYogaPoses(): List<YogaEntity>
}
package com.sparshchadha.workout_app.features.food.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sparshchadha.workout_app.features.food.domain.entities.WaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Update
    suspend fun updateGlassesConsumed(waterEntity: WaterEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlassesConsumed(waterEntity: WaterEntity)

    @Query("SELECT * FROM WaterEntity WHERE date = :date AND month = :month AND year = :year LIMIT 1")
    fun getGlassesConsumedOn(date: String, month: String, year: String): Flow<WaterEntity>

    @Query("SELECT EXISTS(SELECT * FROM WaterEntity WHERE date = :date AND month = :month AND year = :year)")
    fun doesRowExist(date: String, month: String, year: String): Int
}
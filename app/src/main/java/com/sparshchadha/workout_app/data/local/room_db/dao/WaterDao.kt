package com.sparshchadha.workout_app.data.local.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sparshchadha.workout_app.data.local.room_db.entities.WaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Update
    suspend fun updateGlassesConsumed(waterEntity: WaterEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlassesConsumed(waterEntity: WaterEntity)

    @Query("SELECT * FROM WaterEntity WHERE date = :date AND month = :month AND year = :year")
    fun getGlassesConsumedOn(date: String, month: String, year: String): Flow<WaterEntity>

    @Query("SELECT EXISTS(SELECT * FROM WaterEntity WHERE id = :id)")
    fun doesRowExist(id : Int) : Boolean
}
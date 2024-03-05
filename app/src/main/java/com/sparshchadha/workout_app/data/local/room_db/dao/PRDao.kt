package com.sparshchadha.workout_app.data.local.room_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sparshchadha.workout_app.data.local.room_db.entities.PersonalRecordsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PRDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPR(pr: PersonalRecordsEntity): Long

    @Update
    suspend fun updatePR(pr: PersonalRecordsEntity)

    @Delete
    suspend fun deletePR(pr: PersonalRecordsEntity)

    @Query("SELECT * FROM PersonalRecordsEntity WHERE category = :forCategory")
    fun getAllPR(forCategory: String): Flow<List<PersonalRecordsEntity>>
}
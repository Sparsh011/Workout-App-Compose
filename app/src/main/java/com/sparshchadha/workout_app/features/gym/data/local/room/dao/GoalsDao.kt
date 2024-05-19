package com.sparshchadha.workout_app.features.gym.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoal(goalEntity: GoalEntity)

    @Query("SELECT * FROM GoalEntity WHERE priority = :priority")
    fun getGoalsByFilter(priority: String): Flow<List<GoalEntity>>

    @Query("SELECT * FROM GoalEntity")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Update
    fun updateGoal(goalEntity: GoalEntity)

    @Delete
    fun deleteGoal(goal: GoalEntity)
}
package com.sparshchadha.workout_app.features.gym.domain.repository

import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun saveGoal(goalEntity: GoalEntity)

    fun getGoalsByFilter(priority: String): Flow<List<GoalEntity>>

    fun getAllGoals(): Flow<List<GoalEntity>>

    fun updateGoal(goalEntity: GoalEntity)
}
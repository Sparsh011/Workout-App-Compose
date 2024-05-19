package com.sparshchadha.workout_app.features.gym.data.repository

import com.sparshchadha.workout_app.features.gym.data.local.room.dao.GoalsDao
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import com.sparshchadha.workout_app.features.gym.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow

class GoalRepositoryImpl(
    private val goalsDao: GoalsDao
): GoalRepository {
    override fun saveGoal(goalEntity: GoalEntity) {
        goalsDao.insertGoal(goalEntity)
    }

    override fun getGoalsByFilter(priority: String): Flow<List<GoalEntity>> {
        return goalsDao.getGoalsByFilter(priority = priority)
    }

    override fun getAllGoals(): Flow<List<GoalEntity>> {
        return goalsDao.getAllGoals()
    }

    override fun updateGoal(goalEntity: GoalEntity) {
        goalsDao.updateGoal(goalEntity)
    }

    override fun deleteGoal(goal: GoalEntity) {
        goalsDao.deleteGoal(goal)
    }
}
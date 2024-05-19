package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateGoalBottomSheet(
    goalEntity: GoalEntity,
    workoutViewModel: WorkoutViewModel
) {
    SetGoals (goalEntity = goalEntity, showAnim = false) {
        workoutViewModel.updateGoal(
            GoalEntity(
                id = goalEntity.id,
                description = it.description,
                deadlineDay = it.deadlineDay,
                deadlineMonth = it.deadlineMonth,
                deadlineYear = it.deadlineYear,
                reps = it.reps,
                weightUnit = it.weightUnit,
                targetWeight = it.targetWeight,
                priority = it.priority
            )
        )
    }
}
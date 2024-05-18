package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import kotlinx.coroutines.delay

@Composable
internal fun ShowGoals(
    goals: List<GoalEntity>,
    updateGoal: (GoalEntity) -> Unit
) {
    var showNoGoalsAnim by remember {
        mutableStateOf(false)
    }
    if (goals.isEmpty()) {
        LaunchedEffect(key1 = Unit) {
            delay(100L)
            showNoGoalsAnim = true
        }
    }


    if (goals.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState()
        ) {
            items(goals) {
                GoalItem(goal = it)
            }
        }
    } else {
        AnimatedVisibility(
            visible = showNoGoalsAnim,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            ),
        ) {
            Column {
                NoResultsFoundOrErrorDuringSearch(
                    globalPaddingValues = PaddingValues(),
                    localPaddingValues = PaddingValues(),
                    message = "No Goals Saved Yet!"
                )
            }
        }
    }
}

@Composable
fun GoalItem(goal: GoalEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.LARGE_PADDING)
    ) {
        Text(text = goal.description, color = ColorsUtil.primaryTextColor)

        if (goal.deadlineDay.isNotBlank() && goal.deadlineMonth.isNotBlank() && goal.deadlineYear.isNotBlank()) {
            Text(
                text = "Deadline: ${goal.deadlineDay}/${goal.deadlineMonth}/${goal.deadlineYear}",
                color = ColorsUtil.primaryTextColor
            )
        }

        goal.reps?.let {
            Text(text = "Reps: $it", color = ColorsUtil.primaryTextColor)
        }

        goal.targetWeight?.let {
            Text(text = "Target Weight: $it kg", color = ColorsUtil.primaryTextColor)
        }

        Text(text = "Priority: ${goal.priority}", color = ColorsUtil.primaryTextColor)
    }
}
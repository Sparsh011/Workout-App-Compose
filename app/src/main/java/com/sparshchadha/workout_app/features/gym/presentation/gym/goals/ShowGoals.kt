package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.sparshchadha.workout_app.features.gym.domain.entities.GoalEntity
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.ui_state.NoResultsFoundOrErrorDuringSearch
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import kotlinx.coroutines.delay

@Composable
internal fun ShowGoals(
    goals: List<GoalEntity>,
    updateGoal: (GoalEntity) -> Unit,
    deleteGoal: (GoalEntity) -> Unit
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
                GoalItem(
                    goal = it,
                    onUpdateGoal = updateGoal,
                    onDeleteGoal = deleteGoal
                )
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
fun GoalItem(
    goal: GoalEntity,
    onUpdateGoal: (GoalEntity) -> Unit,
    onDeleteGoal: (GoalEntity) -> Unit
) {
    var showUpdateDeleteMenu by rememberSaveable {
        mutableStateOf(false)
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.LARGE_PADDING, vertical = Dimensions.SMALL_PADDING)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.description,
                    color = ColorsUtil.primaryTextColor,
                    fontSize = 20.nonScaledSp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(4f)
                )

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showUpdateDeleteMenu = true
                        },
                    tint = ColorsUtil.primaryTextColor
                )
            }

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

            CustomDivider()
        }

        DropdownMenu(
            expanded = showUpdateDeleteMenu,
            onDismissRequest = { showUpdateDeleteMenu = false }
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = ColorsUtil.targetAchievedColor
                        )
                        Spacer(modifier = Modifier.width(Dimensions.MEDIUM_PADDING))
                        Text(text = "Edit")
                    }
                },
                onClick = {
                    onUpdateGoal(goal)
                    showUpdateDeleteMenu = false
                }
            )
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = ColorsUtil.noAchievementColor
                        )
                        Spacer(modifier = Modifier.width(Dimensions.MEDIUM_PADDING))
                        Text(text = "Delete")
                    }
                },
                onClick = {
                    onDeleteGoal(goal)
                    showUpdateDeleteMenu = false
                }
            )
        }
    }
}
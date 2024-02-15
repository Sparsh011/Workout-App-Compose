package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.GymExercisesPerformed
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.gymExercisesPerformedComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavHostController,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = UtilityScreen.GymExercisesPerformed.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth ->
                    -fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) {
        LaunchedEffect(key1 = true) {
            workoutViewModel.getGymExercisesPerformed()
        }

        val exercisesPerformed = workoutViewModel.gymExercisesPerformed.value
        val uiEventState = workoutViewModel.gymExercisesPerformedOnUIEventState.collectAsStateWithLifecycle()
        val selectedDateAndMonth = workoutViewModel.selectedDateAndMonthForGymExercises.collectAsStateWithLifecycle().value

        GymExercisesPerformed(
            navController = navController,
            exercisesPerformed = exercisesPerformed,
            globalPaddingValues = globalPaddingValues,
            uiEventState = uiEventState,
            selectedDateAndMonth = selectedDateAndMonth,
            getExercisesPerformedOn = {
                workoutViewModel.getGymExercisesPerformed(date = it.first.toString(), month = it.second)
            }
        )
    }
}
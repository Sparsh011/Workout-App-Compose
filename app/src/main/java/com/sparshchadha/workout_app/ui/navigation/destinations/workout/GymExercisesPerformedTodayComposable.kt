package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.GymExercisesPerformedToday
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.gymExercisesPerformedTodayComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavHostController,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = UtilityScreen.GymExercisesPerformedToday.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
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
        workoutViewModel.getGymExercisesPerformedToday()
        val exercisesPerformed = workoutViewModel.gymExercisesPerformedToday.value
        val uiEventState = workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle()

        GymExercisesPerformedToday(
            navController = navController,
            exercisesPerformed = exercisesPerformed,
            globalPaddingValues = globalPaddingValues,
            uiEventState = uiEventState
        )
    }
}
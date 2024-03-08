package com.sparshchadha.workout_app.ui.navigation.destinations.yoga

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaPosesScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.yogaComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = UtilityScreenRoutes.YogaPoses.route,
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
                    fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) {
        val uiEventState by workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle()
        val difficultyLevel = workoutViewModel.getCurrentYogaDifficultyLevel()
        val yogaPoses by workoutViewModel.yogaPosesFromApi

        YogaPosesScreen(
            navController = navController,
            difficultyLevel = difficultyLevel,
            yogaPoses = yogaPoses,
            uiEventState = uiEventState,
            globalPaddingValues = globalPaddingValues,
            workoutViewModel = workoutViewModel
        )
    }
}
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
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaPosesScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.yogaComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    yogaPoses: YogaPosesDto?,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = UtilityScreen.YogaPoses.route,
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

        YogaPosesScreen(
            navController = navController,
            difficultyLevel = difficultyLevel,
            yogaPoses = yogaPoses,
            uiEventState = uiEventState,
            saveYogaPose = { yogaEntity ->
                workoutViewModel.saveYogaPose(
                    yogaEntity
                )
            },
            globalPaddingValues = globalPaddingValues
        )
    }
}
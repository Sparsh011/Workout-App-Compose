package com.sparshchadha.workout_app.ui.navigation.destinations.yoga

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.features.yoga.presentation.yoga.YogaPosesScreen
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

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

        YogaPosesScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            workoutViewModel = workoutViewModel
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.gym.presentation.gym.GymActivityScreen
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes

fun NavGraphBuilder.gymActivityComposable(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {
    composable(
        route = UtilityScreenRoutes.GymActivityScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            )
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        GymActivityScreen(
            navController = navController,
            workoutViewModel = workoutViewModel
        )
    }
}
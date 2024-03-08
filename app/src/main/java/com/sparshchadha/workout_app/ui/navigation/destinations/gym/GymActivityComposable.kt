package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.screens.workout.gym.GymActivityScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.gymActivityComposable(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = UtilityScreenRoutes.GymActivityScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        GymActivityScreen(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )
    }
}
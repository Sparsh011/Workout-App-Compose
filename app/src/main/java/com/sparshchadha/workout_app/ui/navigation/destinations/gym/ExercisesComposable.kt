package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.features.gym.presentation.gym.ExercisesScreen
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

fun NavGraphBuilder.gymExercisesComposable(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
) {
    composable(
        arguments = listOf(navArgument("category") { type = NavType.StringType }),
        route = UtilityScreenRoutes.ExercisesScreen.route,
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
    ) { backStackEntry ->

        ExercisesScreen(
            navController = navController,
            category = backStackEntry.arguments?.getString("category"),
            globalPaddingValues = globalPaddingValues,
            workoutViewModel = workoutViewModel
        )
    }
}
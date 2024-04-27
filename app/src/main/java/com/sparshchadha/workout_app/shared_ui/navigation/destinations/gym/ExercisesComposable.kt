package com.sparshchadha.workout_app.shared_ui.navigation.destinations.gym

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.features.gym.presentation.gym.ExercisesScreen
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes

fun NavGraphBuilder.gymExercisesComposable(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
    toggleBottomBarVisibility: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel,
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
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        ExercisesScreen(
            navController = navController,
            category = backStackEntry.arguments?.getString("category"),
            globalPaddingValues = globalPaddingValues,
            workoutViewModel = workoutViewModel,
            sharedViewModel = sharedViewModel
        )
    }
}
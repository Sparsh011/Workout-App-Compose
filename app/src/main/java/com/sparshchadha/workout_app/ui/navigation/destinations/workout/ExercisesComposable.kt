package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.ExercisesScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.exercisesComposable(
    navController: NavController,
    exercises: GymWorkoutsDto?,
    workoutViewModel: WorkoutViewModel
) {
    composable(
        arguments = listOf(navArgument("category") { type = NavType.StringType }),
        route = UtilityScreen.ExercisesScreen.route,
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
    ) { backStackEntry ->
        val uiEventState by workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle()

        ExercisesScreen(
            navController = navController,
            category = backStackEntry.arguments?.getString("category"),
            exercises = exercises,
            uiEventState = uiEventState
        )
    }
}
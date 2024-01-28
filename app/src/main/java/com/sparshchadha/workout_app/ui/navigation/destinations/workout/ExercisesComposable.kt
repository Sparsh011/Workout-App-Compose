package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.ExercisesScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.gymExercisesComposable(
    navController: NavController,
    gymExercises: GymExercisesDto?,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
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
            exercises = gymExercises,
            uiEventState = uiEventState,
            globalPaddingValues = globalPaddingValues
        )
    }
}
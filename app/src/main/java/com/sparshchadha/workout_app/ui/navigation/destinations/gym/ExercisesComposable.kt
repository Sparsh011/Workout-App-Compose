package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.screens.workout.gym.ExercisesScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

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
        val uiEventState by workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle()

        val gymExercises by workoutViewModel.gymExercisesFromApi

        ExercisesScreen(
            navController = navController,
            category = backStackEntry.arguments?.getString("category"),
            exercises = gymExercises,
            uiEventState = uiEventState,
            globalPaddingValues = globalPaddingValues,
            saveExercise = { gymExerciseEntity ->
                workoutViewModel.addGymExerciseToWorkout(gymExercisesEntity = gymExerciseEntity)
            },
            workoutViewModel = workoutViewModel
        )
    }
}
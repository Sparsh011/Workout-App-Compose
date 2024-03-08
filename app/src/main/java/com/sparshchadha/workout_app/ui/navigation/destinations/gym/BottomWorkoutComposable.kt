package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.ui.screens.workout.GymAndYogaWorkoutHomeScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.bottomWorkoutComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = ScreenRoutes.WorkoutScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        GymAndYogaWorkoutHomeScreen(
            difficultyLevels = listOf(
                DifficultyLevel.BEGINNER,
                DifficultyLevel.INTERMEDIATE,
                DifficultyLevel.EXPERT
            ),
            workoutViewModel = workoutViewModel,
            navController = navController,
            gymWorkoutCategories = listOf(
                GymWorkoutCategories.MUSCLE.name.lowercase().capitalize(),
                GymWorkoutCategories.DIFFICULTY.name.lowercase().capitalize(),
                GymWorkoutCategories.PROGRAM.name.lowercase().capitalize(),
                GymWorkoutCategories.SEARCH.name.lowercase().capitalize()
            ),
            globalPaddingValues = globalPaddingValues
        )
    }
}
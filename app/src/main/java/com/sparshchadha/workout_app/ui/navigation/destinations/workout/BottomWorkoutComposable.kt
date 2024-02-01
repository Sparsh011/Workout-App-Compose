package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.ui.screens.workout.GymAndYogaWorkoutHomeScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.bottomWorkoutComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = BottomBarScreen.WorkoutScreen.route,
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
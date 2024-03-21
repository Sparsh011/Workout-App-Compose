package com.sparshchadha.workout_app.shared_ui.navigation.destinations.gym

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.shared_ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.shared_ui.screens.workout.GymAndYogaWorkoutHomeScreen
import com.sparshchadha.workout_app.util.Extensions.capitalize

fun NavGraphBuilder.bottomWorkoutComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
    newsViewModel: NewsViewModel,
    yogaViewModel: YogaViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
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
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(true)
        }
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
            globalPaddingValues = globalPaddingValues,
            newsViewModel = newsViewModel,
            yogaViewModel = yogaViewModel
        )
    }
}
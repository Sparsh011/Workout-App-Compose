package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.SelectExerciseCategory
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.workoutCategoryComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController
) {
    composable(
        route = UtilityScreen.SelectExerciseCategory.route,
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
        SelectExerciseCategory(
            workoutViewModel = workoutViewModel,
            navController = navController
        )
    }
}
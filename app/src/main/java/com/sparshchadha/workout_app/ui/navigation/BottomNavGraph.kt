package com.sparshchadha.workout_app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerComposable
import com.sparshchadha.workout_app.ui.screens.workout.WorkoutScreenComposable

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.CalorieTracker.route) {
        composable(
            route = BottomBarScreen.WorkoutScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            },
            exitTransition = {
                slideOutHorizontally (
                    targetOffsetX = {
                        fullWidth -> -fullWidth
                    },
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            }
        ){
            WorkoutScreenComposable(
                gymWorkoutCategories = listOf(
                    "Beginner",
                    "Intermediate",
                    "Advanced",
                    "Calisthenics"
                )
            )

        }

        composable(
            route = BottomBarScreen.CalorieTracker.route
        ){
            CalorieTrackerComposable()
        }
    }
}


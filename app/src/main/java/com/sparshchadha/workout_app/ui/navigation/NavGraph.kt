package com.sparshchadha.workout_app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerComposable
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.SearchDishScreen
import com.sparshchadha.workout_app.ui.screens.profile.ProfileScreenComposable
import com.sparshchadha.workout_app.ui.screens.workout.WorkoutScreenComposable
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaDifficultyLevels
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaPosesScreen
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    searchFoodViewModel: SearchFoodViewModel,
    workoutViewModel: WorkoutViewModel
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.CalorieTracker.route) {
        // Workout Tracker
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
            WorkoutScreenComposable(
                gymWorkoutCategories = listOf(
                    YogaDifficultyLevels.BEGINNER,
                    YogaDifficultyLevels.INTERMEDIATE,
                    YogaDifficultyLevels.EXPERT
                ),
                workoutViewModel = workoutViewModel,
                navController = navController
            )
        }

        // Calorie Tracker
        composable(
            route = BottomBarScreen.CalorieTracker.route
        ) {
            CalorieTrackerComposable(navController = navController)
        }

        // Search Screen
        composable(
            route = UtilityScreen.SearchFood.route,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(durationMillis = 1000),
                    initialOffsetY = { it / 2 }
                )
            },
            exitTransition = {
                slideOutVertically()
            }
        ) {
            SearchDishScreen(searchFoodViewModel = searchFoodViewModel, paddingValues = paddingValues, onCloseClicked = {
                navController.popBackStack()
            })
        }

        // Profile Screen
        composable(
            route = BottomBarScreen.ProfileScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
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
            ProfileScreenComposable()
        }

        // Yoga Screen
        composable(
            route = UtilityScreen.YogaPoses.route,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(durationMillis = 1000),
                    initialOffsetY = { -it / 2 }
                )
            },
        ) {
            YogaPosesScreen(workoutViewModel = workoutViewModel)
        }
    }
}


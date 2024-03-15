package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.screens.shared.SavedItemsScreen
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

fun NavGraphBuilder.savedItemsScreenComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel
) {
    composable(
        route = UtilityScreenRoutes.SavedItemsScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) { backStackEntry ->
        val category = backStackEntry.arguments?.getString("category")

        SavedItemsScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            category = category ?: "Unable To Get Exercises"
        )
    }
}
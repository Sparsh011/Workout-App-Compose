package com.sparshchadha.workout_app.shared_ui.navigation.destinations.shared

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.shared_ui.screens.shared.SavedItemsScreen

fun NavGraphBuilder.savedItemsScreenComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel,
    yogaViewModel: YogaViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
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
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        SavedItemsScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodAndWaterViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            category = category ?: "Something Went Wrong!",
            yogaViewModel = yogaViewModel
        )
    }
}
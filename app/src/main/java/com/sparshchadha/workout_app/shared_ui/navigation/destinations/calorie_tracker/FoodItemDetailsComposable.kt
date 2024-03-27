package com.sparshchadha.workout_app.shared_ui.navigation.destinations.calorie_tracker

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.features.food.presentation.calorie_tracker.FoodItemDetails
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes

fun NavGraphBuilder.foodItemDetailsComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {
    composable(
        route = UtilityScreenRoutes.FoodItemDetailsScreen.route,
        arguments = listOf(navArgument("foodItemId") { type = NavType.IntType }),
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
                    fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) { navBackStackEntry ->
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        FoodItemDetails(
            navController = navController,
            foodItemId = navBackStackEntry.arguments?.getInt("foodItemId"),
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel
        )
    }
}
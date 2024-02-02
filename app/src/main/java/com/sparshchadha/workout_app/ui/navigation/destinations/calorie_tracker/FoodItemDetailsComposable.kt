package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.FoodItemDetails
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

fun NavGraphBuilder.foodItemDetailsComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
) {
    composable(
        route = UtilityScreen.FoodItemDetailsScreen.route,
        arguments = listOf(navArgument("foodItemId") { type = NavType.IntType })
    ) { navBackStackEntry ->
        FoodItemDetails(
            navController = navController,
            foodItemId = navBackStackEntry.arguments?.getInt("foodItemId"),
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel
        )
    }
}
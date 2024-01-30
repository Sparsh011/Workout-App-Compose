package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerScreen
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

fun NavGraphBuilder.calorieTrackerComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
) {
    composable(
        route = BottomBarScreen.CalorieTracker.route
    ) {
        foodItemsViewModel.getFoodItemsConsumedToday()
        val foodItemsConsumedToday = foodItemsViewModel.savedFoodItems.value
        CalorieTrackerScreen(
            navController = navController,
            paddingValues = globalPaddingValues,
            foodItemsConsumedToday = foodItemsConsumedToday
        )
    }
}
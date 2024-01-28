package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerScreen

fun NavGraphBuilder.calorieTrackerComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = BottomBarScreen.CalorieTracker.route
    ) {
        CalorieTrackerScreen(navController = navController, paddingValues = globalPaddingValues)
    }
}
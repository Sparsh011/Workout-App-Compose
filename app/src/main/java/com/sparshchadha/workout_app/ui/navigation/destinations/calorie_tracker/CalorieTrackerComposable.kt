package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.food.presentation.calorie_tracker.CalorieTrackerScreen
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.bottomCalorieTrackerComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    profileViewModel: ProfileViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {
    composable(
        route = ScreenRoutes.CalorieTracker.route,
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
        CalorieTrackerScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodAndWaterViewModel = foodItemsViewModel,
            profileViewModel = profileViewModel
        )
    }
}
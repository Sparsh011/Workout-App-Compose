package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerScreen
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.calorieTrackerComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
    profileViewModel: ProfileViewModel
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

        CalorieTrackerScreen(
            navController = navController,
            paddingValues = globalPaddingValues,
            getDishesConsumedOnSelectedDayAndMonth = {
                foodItemsViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
            },
            saveNewCaloriesGoal = {
                profileViewModel.saveCaloriesGoal(it)
            },
            removeFoodItem = {
                foodItemsViewModel.removeFoodItem(foodItem = it)
            },
            foodItemsViewModel = foodItemsViewModel,
            profileViewModel = profileViewModel
        )
    }
}
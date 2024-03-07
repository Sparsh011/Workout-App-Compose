package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
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
        route = BottomBarScreen.CalorieTracker.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {

        LaunchedEffect(key1 = true) {
            foodItemsViewModel.getFoodItemsConsumedOn()
        }

        val caloriesGoal = profileViewModel.caloriesGoal.collectAsStateWithLifecycle(initialValue = "0").value
        val caloriesConsumed = foodItemsViewModel.caloriesConsumed.value ?: "0"
        val selectedDateAndMonth = foodItemsViewModel.selectedDateAndMonthForFoodItems.collectAsStateWithLifecycle().value

        CalorieTrackerScreen(
            navController = navController,
            paddingValues = globalPaddingValues,
            getDishesConsumedOnSelectedDayAndMonth = {
                foodItemsViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
            },
            saveNewCaloriesGoal = {
                profileViewModel.saveCaloriesGoal(it)
            },
            caloriesGoal = caloriesGoal,
            caloriesConsumed = caloriesConsumed,
            selectedDateAndMonth = selectedDateAndMonth,
            removeFoodItem = {
                foodItemsViewModel.removeFoodItem(foodItem = it)
            },
            foodItemsViewModel = foodItemsViewModel
        )
    }
}
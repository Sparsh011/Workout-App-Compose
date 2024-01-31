package com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CalorieTrackerScreen
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.calorieTrackerComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
) {
    composable(
        route = BottomBarScreen.CalorieTracker.route
    ) {

        LaunchedEffect(key1 = true) {
            foodItemsViewModel.getFoodItemsConsumedOn()
            foodItemsViewModel.getCaloriesGoal()
        }

        val foodItemsConsumed = foodItemsViewModel.savedFoodItems.value
        val caloriesGoal = foodItemsViewModel.caloriesGoal.value ?: "0"
        val caloriesConsumed = foodItemsViewModel.caloriesConsumed.value ?: "0"

        CalorieTrackerScreen(
            navController = navController,
            paddingValues = globalPaddingValues,
            foodItemsConsumed = foodItemsConsumed,
            getDishesConsumedOnSelectedDayAndMonth = {
                foodItemsViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
            },
            saveNewCaloriesGoal = {
                foodItemsViewModel.addOrUpdateCaloriesGoal(caloriesGoal = it.toInt())
            },
            caloriesGoal = caloriesGoal,
            caloriesConsumed = caloriesConsumed
        )
    }
}
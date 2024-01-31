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
        }

        val foodItemsConsumedToday = foodItemsViewModel.savedFoodItems.value

        CalorieTrackerScreen(
            navController = navController,
            paddingValues = globalPaddingValues,
            foodItemsConsumedToday = foodItemsConsumedToday,
            getDishesConsumedOnSelectedDayAndMonth = {
                foodItemsViewModel.getFoodItemsConsumedOn(it.first.toString(), it.second)
            }
        )
    }
}
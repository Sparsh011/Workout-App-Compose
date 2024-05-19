package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.SearchScreen

fun NavGraphBuilder.searchComposable(
    foodAndWaterViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
    toggleBottomBarVisibility: (Boolean) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = UtilityScreenRoutes.SearchScreen.route,
        enterTransition = {
            slideInVertically(
                animationSpec = tween(durationMillis = 1000),
                initialOffsetY = { it / 2 }
            )
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { 0 }
            )
        }
    ) { backStackEntry ->
        val searchFor = backStackEntry.arguments?.getString("searchFor")
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        SearchScreen(
            searchFoodViewModel = foodAndWaterViewModel,
            paddingValues = globalPaddingValues,
            onCloseClicked = {
                navController.popBackStack()
            },
            searchFor = searchFor,
            workoutViewModel = workoutViewModel,
            navController = navController,
            foodAndWaterViewModel = foodAndWaterViewModel,
            sharedViewModel = sharedViewModel
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.SearchScreen
import com.sparshchadha.workout_app.viewmodel.FoodAndWaterViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.searchComposable(
    foodAndWaterViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
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
        var foodUIStateEvent: WorkoutViewModel.UIEvent? = null

        when (searchFor) {
            "food" -> {
                foodUIStateEvent =
                    foodAndWaterViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value
            }
        }

        SearchScreen(
            searchFoodViewModel = foodAndWaterViewModel,
            paddingValues = globalPaddingValues,
            onCloseClicked = {
                navController.popBackStack()
            },
            searchFor = searchFor,
            workoutViewModel = workoutViewModel,
            foodUIStateEvent = foodUIStateEvent,
            navController = navController,
            foodAndWaterViewModel = foodAndWaterViewModel
        )
    }
}
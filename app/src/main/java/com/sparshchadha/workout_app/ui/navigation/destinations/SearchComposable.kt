package com.sparshchadha.workout_app.ui.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.SearchScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.searchComposable(
    searchFoodViewModel: SearchFoodViewModel,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    composable(
        route = UtilityScreen.SearchScreen.route,
        enterTransition = {
            slideInVertically(
                animationSpec = tween(durationMillis = 1000),
                initialOffsetY = { it / 2 }
            )
        },
        exitTransition = {
            slideOutVertically()
        }
    ) { backStackEntry ->
        val searchFor = backStackEntry.arguments?.getString("searchFor")
        val dishes = searchFoodViewModel.foodItems.value
        val exercises = workoutViewModel.exercises.value
        var workoutUIStateEvent : WorkoutViewModel.UIEvent? = null
        var foodUIStateEvent : WorkoutViewModel.UIEvent? = null

        when(searchFor) {
            "food" -> {
                foodUIStateEvent = searchFoodViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value
            }

            "exercises" -> {
                workoutUIStateEvent = workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value
            }
        }

        SearchScreen(
            searchFoodViewModel = searchFoodViewModel,
            paddingValues = paddingValues,
            onCloseClicked = {
                navController.popBackStack()
            },
            searchFor = searchFor,
            workoutViewModel = workoutViewModel,
            dishes = dishes,
            exercises = exercises,
            workoutUIStateEvent = workoutUIStateEvent,
            foodUIStateEvent = foodUIStateEvent
        )
    }
}
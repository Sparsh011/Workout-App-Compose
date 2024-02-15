package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.SearchScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.searchComposable(
    foodItemsViewModel: FoodItemsViewModel,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
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
            slideOutVertically(
                targetOffsetY = { 0 }
            )
        }
    ) { backStackEntry ->
        val searchFor = backStackEntry.arguments?.getString("searchFor")
        val dishes = foodItemsViewModel.foodItemsFromApi.value
        val exercises = workoutViewModel.gymExercisesFromApi.value
        var workoutUIStateEvent : WorkoutViewModel.UIEvent? = null
        var foodUIStateEvent : WorkoutViewModel.UIEvent? = null

        when(searchFor) {
            "food" -> {
                foodUIStateEvent = foodItemsViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value
            }

            "exercises" -> {
                workoutUIStateEvent = workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value
            }
        }

        SearchScreen(
            searchFoodViewModel = foodItemsViewModel,
            paddingValues = globalPaddingValues,
            onCloseClicked = {
                navController.popBackStack()
            },
            searchFor = searchFor,
            workoutViewModel = workoutViewModel,
            dishes = dishes,
            exercises = exercises,
            workoutUIStateEvent = workoutUIStateEvent,
            foodUIStateEvent = foodUIStateEvent,
            saveFoodItemWithQuantity = {
                foodItemsViewModel.saveFoodItem(foodItemEntity = it)
            },
            saveExercise = {
                workoutViewModel.saveGymExercise(gymExercisesEntity = it)
            },
            navController = navController
        )
    }
}
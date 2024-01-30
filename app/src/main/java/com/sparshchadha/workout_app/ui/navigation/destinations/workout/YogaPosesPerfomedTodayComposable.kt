package com.sparshchadha.workout_app.ui.navigation.destinations.workout

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaPosesPerformedToday
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

fun NavGraphBuilder.yogaPosesPerformedTodayComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues
) {
    composable(
        route = UtilityScreen.YogaPosesPerformedToday.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth ->
                    -fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) {
        workoutViewModel.getYogaPosesPerformedToday()
        val yogaPosesPerformedToday = workoutViewModel.yogaPosesPerformedToday.value
        val uiEventState = workoutViewModel.uiEventStateFlow.collectAsStateWithLifecycle().value

        YogaPosesPerformedToday(
            yogaPosesPerformedToday = yogaPosesPerformedToday,
            uiEventState = uiEventState,
            globalPaddingValues = globalPaddingValues,
            onBackButtonPressed = {
                navController.popBackStack(
                    route = BottomBarScreen.WorkoutScreen.route,
                    inclusive = false
                )
            }
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.screens.reminders.RemindersScreen
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel

fun NavGraphBuilder.remindersComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    remindersViewModel: RemindersViewModel,
) {
    composable(
        route = ScreenRoutes.RemindersScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        RemindersScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            remindersViewModel = remindersViewModel
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.reminders.presentation.reminders.RemindersScreen
import com.sparshchadha.workout_app.features.reminders.presentation.viewmodels.RemindersViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.bottomRemindersComposable(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    remindersViewModel: RemindersViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit

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
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(true)
        }

        RemindersScreen(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            remindersViewModel = remindersViewModel
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.profileComposable(globalPaddingValues: PaddingValues) {
    composable(
        route = BottomBarScreen.ProfileScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        ProfileScreen(
            globalPaddingValues = globalPaddingValues
        )
    }
}
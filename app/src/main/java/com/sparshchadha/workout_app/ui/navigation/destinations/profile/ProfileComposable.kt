package com.sparshchadha.workout_app.ui.navigation.destinations.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.profile.ProfileScreenComposable

fun NavGraphBuilder.profileComposable(

) {
    composable(
        route = BottomBarScreen.ProfileScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        ProfileScreenComposable()
    }
}
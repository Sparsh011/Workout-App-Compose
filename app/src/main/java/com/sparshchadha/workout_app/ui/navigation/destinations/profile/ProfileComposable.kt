package com.sparshchadha.workout_app.ui.navigation.destinations.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.profile.presentation.profile.ProfileScreen
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes

fun NavGraphBuilder.profileComposable(
    globalPaddingValues: PaddingValues,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    composable(
        route = ScreenRoutes.ProfileScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        ProfileScreen(
            globalPaddingValues = globalPaddingValues,
            navController = navController,
            profileViewModel = profileViewModel
        )
    }
}
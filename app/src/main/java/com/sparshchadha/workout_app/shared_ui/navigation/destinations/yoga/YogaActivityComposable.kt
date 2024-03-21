package com.sparshchadha.workout_app.shared_ui.navigation.destinations.yoga

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.yoga.presentation.screens.YogaActivityScreen
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes

fun NavGraphBuilder.yogaActivityComposable(
    navController: NavController,
    yogaViewModel: YogaViewModel,
    globalPaddingValues: PaddingValues,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {
    composable(
        route = UtilityScreenRoutes.YogaActivityScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        LaunchedEffect(key1 = Unit) {
            toggleBottomBarVisibility(false)
        }
        YogaActivityScreen(
            navController = navController,
            yogaViewModel = yogaViewModel,
            globalPaddingValues = globalPaddingValues
        )
    }
}
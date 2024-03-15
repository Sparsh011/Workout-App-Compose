package com.sparshchadha.workout_app.ui.navigation.destinations.yoga

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.yoga.presentation.screens.GetYogaPosesPerformedOnParticularDay
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.yogaPosesPerformedTodayComposable(
    yogaViewModel: YogaViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = UtilityScreenRoutes.YogaPosesPerformed.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth ->
                    fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) {

        GetYogaPosesPerformedOnParticularDay(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            yogaViewModel = yogaViewModel
        )
    }
}
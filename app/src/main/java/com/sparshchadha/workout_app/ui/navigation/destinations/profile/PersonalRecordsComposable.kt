package com.sparshchadha.workout_app.ui.navigation.destinations.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.features.gym.presentation.gym.PersonalRecordsScreen
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.personalRecordsComposable(
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
    navController: NavController,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {
    composable(
        route = UtilityScreenRoutes.PersonalRecordsScreen.route,
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
        PersonalRecordsScreen(
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            navController = navController
        )
    }
}
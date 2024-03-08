package com.sparshchadha.workout_app.ui.navigation.destinations.gym

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.screens.workout.gym.GymExercisesPerformed
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.gymExercisesPerformedComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = UtilityScreenRoutes.GymExercisesPerformed.route,
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

        GymExercisesPerformed(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
           workoutViewModel = workoutViewModel
        )
    }
}
package com.sparshchadha.workout_app.ui.navigation.destinations.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.PersonalRecordsScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.personalRecordsScreen(
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
    navController: NavController
) {
    composable(
        route = UtilityScreen.PersonalRecordsScreen.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        PersonalRecordsScreen(
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            navController = navController
        )
    }
}
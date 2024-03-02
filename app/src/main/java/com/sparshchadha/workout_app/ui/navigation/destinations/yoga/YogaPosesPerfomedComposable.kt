package com.sparshchadha.workout_app.ui.navigation.destinations.yoga

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.yoga.GetYogaPosesPerformedOnParticularDay
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.yogaPosesPerformedTodayComposable(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
) {
    composable(
        route = UtilityScreen.YogaPosesPerformed.route,
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
        LaunchedEffect(
            key1 = true,
            block = {
                workoutViewModel.getYogaPosesPerformedOn()
            }
        )

        val yogaPosesPerformedToday = workoutViewModel.yogaPosesPerformed.value
        val uiEventState =
            workoutViewModel.yogaPosesPerformedOnUIEventState.collectAsStateWithLifecycle().value
        val selectedDayAndMonth =
            workoutViewModel.selectedDateAndMonthForYogaPoses.collectAsStateWithLifecycle().value

        GetYogaPosesPerformedOnParticularDay(
            yogaPosesPerformedToday = yogaPosesPerformedToday,
            uiEventState = uiEventState,
            globalPaddingValues = globalPaddingValues,
            onBackButtonPressed = {
                navController.popBackStack(
                    route = BottomBarScreen.WorkoutScreen.route,
                    inclusive = false
                )
            },
            getYogaPosesPerformedOn = {
                workoutViewModel.getYogaPosesPerformedOn(
                    date = it.first.toString(),
                    month = it.second
                )
            },
            selectedMonth = selectedDayAndMonth?.second ?: "January",
            selectedDay = selectedDayAndMonth?.first ?: 1,
            removePose = {
                workoutViewModel.removeYogaPose(it)
            }
        )
    }
}
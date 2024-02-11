package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun ExerciseDetailsScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
) {
    val exercise = workoutViewModel.exerciseDetails.value
    Scaffold (
        topBar = {
            ScaffoldTopBar(
                topBarDescription = exercise?.name ?: "Unable To Get Exercise Name",
                onBackButtonPressed = {
                    navController.popBackStack(UtilityScreen.ExercisesScreen.route, false)
                }
            )
        },
        containerColor = Color.White
    ){ localPaddingValues ->
        exercise?.let {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(bottom = localPaddingValues.calculateTopPadding(), top = globalPaddingValues.calculateBottomPadding())
            ) {
                ExerciseSubTitlesAndDescription(subTitle = "Exercise Name", description = exercise.name)
                ExerciseSubTitlesAndDescription(subTitle = "Difficulty", description = exercise.difficulty)
                ExerciseSubTitlesAndDescription(subTitle = "Muscle", description = exercise.muscle)
                ExerciseSubTitlesAndDescription(subTitle = "Equipment Required", description = exercise.equipment)
                ExerciseSubTitlesAndDescription(subTitle = "Instructions", description = exercise.instructions)
            }
        }
    }
}
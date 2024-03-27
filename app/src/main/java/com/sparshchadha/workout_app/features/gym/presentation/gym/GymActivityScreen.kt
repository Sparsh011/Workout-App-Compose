package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun GymActivityScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
) {
    val performedExercises by workoutViewModel.allExercisesPerformed.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getAllExercisesPerformed()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) {
        ScaffoldTopBar(
            topBarDescription = "All Exercises Performed",
            onBackButtonPressed = {
                navController.popBackStack()
            }
        )

        // Add graph here to show number of sets performed on each day

        if (!performedExercises.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SMALL_PADDING)
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(ColorsUtil.bottomBarColor)
            ) {
                items(performedExercises ?: emptyList()) {
                    ExercisePerformed(
                        exercise = it,
                        onClick = {

                        }
                    )
                }
            }
        } else {
            NoSavedItem()
        }
    }
}

@Composable
fun ExercisePerformed(
    exercise: GymExercisesEntity,
    onClick: () -> Unit
) {
    Text(
        text = "${exercise.exerciseDetails?.name}, ${exercise.setsPerformed} sets",
        color = primaryTextColor,
        fontSize = 18.nonScaledSp,
        modifier = Modifier
            .padding(MEDIUM_PADDING)
            .clickable {
                onClick()
            }
    )
}

package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.ShowQuantityOrSetsPicker
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun ExerciseDetailsScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
) {
    val exercise = workoutViewModel.exerciseDetails.value
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = exercise?.name ?: "Unable To Get Exercise Name",
                onBackButtonPressed = {
                    navController.popBackStack()
                },
                topBarVerticalPadding = 0.dp
            )
        },
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->
        exercise?.let {
            var shouldShowQuantityOrSetsPicker by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding(),
                        start = MEDIUM_PADDING,
                        end = MEDIUM_PADDING,
                    )
            ) {
                ExerciseSubTitlesAndDescription(
                    subTitle = "Difficulty",
                    description = exercise.difficulty
                )
                ExerciseSubTitlesAndDescription(subTitle = "Targets", description = exercise.muscle)
                ExerciseSubTitlesAndDescription(
                    subTitle = "Equipment Required",
                    description = exercise.equipment
                )
                ExerciseSubTitlesAndDescription(
                    subTitle = "Instructions",
                    description = exercise.instructions
                )

                Button(
                    onClick = {
                        workoutViewModel.saveExerciseToDB(
                            GymExercisesEntity(
                                date = "",
                                month = "",
                                setsPerformed = -1,
                                exerciseDetails = it,
                                hour = -1,
                                minutes = -1,
                                seconds = -1,
                                isPerformed = false
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = LARGE_PADDING + MEDIUM_PADDING, vertical = SMALL_PADDING)
                ) {
                    Text(text = "Save Exercise", color = White)
                }

                Button(
                    onClick = {
                        shouldShowQuantityOrSetsPicker = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = LARGE_PADDING + MEDIUM_PADDING, vertical = SMALL_PADDING)
                ) {
                    Text(text = "Add Exercise To Today's Workout", color = White)
                }
            }


            if (shouldShowQuantityOrSetsPicker) {
                ShowQuantityOrSetsPicker(
                    hideQuantityOrSetsPickerBottomSheet = {
                        shouldShowQuantityOrSetsPicker = false
                    },
                    saveQuantityOrSets = { setsPerformed ->
                        workoutViewModel.addGymExerciseToWorkout(
                            GymExercisesEntity(
                                date = HelperFunctions.getCurrentDateAndMonth().first.toString(),
                                month = HelperFunctions.getCurrentDateAndMonth().second,
                                setsPerformed = setsPerformed,
                                exerciseDetails = exercise
                            )
                        )

                        shouldShowQuantityOrSetsPicker = false
                    },
                    title = "Sets"
                )
            }
        }
    }
}
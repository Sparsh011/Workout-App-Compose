package com.sparshchadha.workout_app.ui.screens.workout.gym

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
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

private const val TAG = "ExerciseDetailsScreen"

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
                topBarVerticalPadding = MEDIUM_PADDING
            )
        },
        containerColor = scaffoldBackgroundColor,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)
                        .fillMaxWidth()
                        .padding(SMALL_PADDING)
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                Log.d(TAG, "ExerciseDetailsScreen: Save clicked")
                            }
                            .weight(1f),
                        text = "Save",
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .clickable {
                                Log.d(TAG, "ExerciseDetailsScreen: Share clicked")
                            }
                            .weight(1f),
                        text = "Share",
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Add",
                        modifier = Modifier
                            .clickable {
                                Log.d(TAG, "ExerciseDetailsScreen: add clicked")
                            }
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        textAlign = TextAlign.Center,
                        text = "Helo",
                        modifier = Modifier
                            .clickable {
                                Log.d(TAG, "ExerciseDetailsScreen: Hello clicked")
                            }
                            .weight(1f),
                    )
                }
            }
        },
        modifier = Modifier.padding(bottom = globalPaddingValues.calculateBottomPadding()),
        floatingActionButtonPosition = FabPosition.Center
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LARGE_PADDING + MEDIUM_PADDING,
                            vertical = SMALL_PADDING
                        )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LARGE_PADDING + MEDIUM_PADDING,
                            vertical = SMALL_PADDING
                        )
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
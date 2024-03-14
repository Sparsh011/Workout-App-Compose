package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.ShowQuantityOrSetsPicker
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
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
    val selectedExercise = workoutViewModel.selectedExercise.value
    var shouldShowQuantityOrSetsPicker by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = exercise?.name ?: selectedExercise?.name ?: "Unable To Get Name!",
                onBackButtonPressed = {
                    navController.popBackStack()
                },
                topBarVerticalPadding = MEDIUM_PADDING
            )
        },
        containerColor = scaffoldBackgroundColor,
        modifier = Modifier.padding(bottom = globalPaddingValues.calculateBottomPadding()),
        floatingActionButtonPosition = FabPosition.Center
    ) { localPaddingValues ->
        if (exercise != null) {

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
                    title = "Difficulty",
                    description = exercise.difficulty,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Targets",
                    description = exercise.muscle,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Equipment Required",
                    description = exercise.equipment,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Instructions",
                    description = exercise.instructions,
                )

                Spacer(modifier = Modifier.height(MEDIUM_PADDING))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING)
                        .clip(RoundedCornerShape(MEDIUM_PADDING))
                        .background(bottomBarColor)
                        .padding(MEDIUM_PADDING)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                            },
                        tint = primaryPurple
                    )

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                shouldShowQuantityOrSetsPicker = true

                            },
                        tint = primaryPurple
                    )

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                workoutViewModel.saveExerciseToDB(
                                    GymExercisesEntity(
                                        date = "",
                                        month = "",
                                        setsPerformed = -1,
                                        exerciseDetails = exercise,
                                        hour = -1,
                                        minutes = -1,
                                        seconds = -1,
                                        isPerformed = false
                                    )
                                )
                            },
                        tint = primaryPurple
                    )
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
        } else if (selectedExercise != null) {
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
                    title = "Difficulty",
                    description = selectedExercise.difficulty,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Targets",
                    description = selectedExercise.muscle,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Equipment Required",
                    description = selectedExercise.equipment,
                )

                ExerciseSubTitlesAndDescription(
                    title = "Instructions",
                    description = selectedExercise.instructions,
                )

                Spacer(modifier = Modifier.height(MEDIUM_PADDING))
            }
        }
    }
}
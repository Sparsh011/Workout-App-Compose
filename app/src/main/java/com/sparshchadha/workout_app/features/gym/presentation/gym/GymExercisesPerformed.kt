package com.sparshchadha.workout_app.features.gym.presentation.gym

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CalendarRow
import com.sparshchadha.workout_app.ui.components.shared.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymExercisesPerformed(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    workoutViewModel: WorkoutViewModel
) {
    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getGymExercisesPerformed()
    }

    val exercisesPerformed =
        workoutViewModel.gymExercisesPerformed.collectAsStateWithLifecycle().value
    val selectedDateAndMonth =
        workoutViewModel.selectedDateAndMonthForGymExercises.collectAsStateWithLifecycle().value

    HandleUIEventsForExercisesPerformedToday(
        exercisesPerformed = exercisesPerformed,
        globalPaddingValues = globalPaddingValues,
        navController = navController,
        selectedDateAndMonth = selectedDateAndMonth,
        getExercisesPerformedOn = {
            workoutViewModel.getGymExercisesPerformed(
                date = it.first.toString(),
                month = it.second
            )
        },
        removeExercise = {
            workoutViewModel.removeExerciseFromDB(it)
        },
        navigateToExerciseDetailsScreen = {
            workoutViewModel.updateExerciseDetails(it)
            navController.navigate(UtilityScreenRoutes.ExerciseDetailScreen.route)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HandleUIEventsForExercisesPerformedToday(
    globalPaddingValues: PaddingValues,
    exercisesPerformed: List<GymExercisesEntity>?,
    navController: NavHostController,
    selectedDateAndMonth: Pair<Int, String>?,
    getExercisesPerformedOn: (Pair<Int, String>) -> Unit,
    removeExercise: (GymExercisesEntity) -> Unit,
    navigateToExerciseDetailsScreen: (GymExercisesDtoItem?) -> Unit
) {
    PopulatePerformedExercises(
        globalPaddingValues = globalPaddingValues,
        exercisesPerformed = exercisesPerformed,
        onBackButtonPressed = {
            navController.popBackStack()
        },
        selectedDateAndMonth = selectedDateAndMonth,
        getExercisesPerformedOn = getExercisesPerformedOn,
        removeExercise = removeExercise,
        navigateToExerciseDetailsScreen = navigateToExerciseDetailsScreen
    )
//    when (event) {
//        is WorkoutViewModel.UIEvent.ShowLoader -> {
//            ShowLoadingScreen()
//        }
//
//        is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
//            PopulatePerformedExercises(
//                globalPaddingValues = globalPaddingValues,
//                exercisesPerformed = exercisesPerformed,
//                onBackButtonPressed = {
//                    navController.popBackStack()
//                },
//                selectedDateAndMonth = selectedDateAndMonth,
//                getExercisesPerformedOn = getExercisesPerformedOn,
//                removeExercise = removeExercise,
//                navigateToExerciseDetailsScreen = navigateToExerciseDetailsScreen
//            )
//        }
//
//        is WorkoutViewModel.UIEvent.ShowError -> {
//            ErrorDuringFetch(errorMessage = event.errorMessage)
//        }
//    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopulatePerformedExercises(
    globalPaddingValues: PaddingValues,
    exercisesPerformed: List<GymExercisesEntity>?,
    onBackButtonPressed: () -> Unit,
    getExercisesPerformedOn: (Pair<Int, String>) -> Unit,
    selectedDateAndMonth: Pair<Int, String>?,
    removeExercise: (GymExercisesEntity) -> Unit,
    navigateToExerciseDetailsScreen: (GymExercisesDtoItem?) -> Unit,
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Exercises Performed Today",
                onBackButtonPressed = onBackButtonPressed
            )
        },
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->

        LazyColumn(
            modifier = Modifier
                .background(scaffoldBackgroundColor)
                .fillMaxSize()
                .padding(
                    top = localPaddingValues.calculateTopPadding(),
                    bottom = globalPaddingValues.calculateBottomPadding()
                )
        ) {
            stickyHeader {
                CalendarRow(
                    getResultsForDateAndMonth = getExercisesPerformedOn,
                    selectedMonth = selectedDateAndMonth?.second ?: "January",
                    selectedDay = selectedDateAndMonth?.first ?: 1,
//                    indicatorColor = HelperFunctions.getAchievementColor(achieved = caloriesConsumed.toInt(), target = caloriesGoal.toInt())
                )
            }

            if (exercisesPerformed.isNullOrEmpty()) {
                item {
                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Exercise Performed!",
                        localPaddingValues = localPaddingValues,
                    )
                }
            } else {
                items(exercisesPerformed) {
                    ExerciseEntity(
                        exerciseEntity = it,
                        removeExercise = removeExercise,
                        navigateToExerciseDetailsScreen = navigateToExerciseDetailsScreen
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEntity(
    exerciseEntity: GymExercisesEntity,
    removeExercise: (GymExercisesEntity) -> Unit,
    navigateToExerciseDetailsScreen: (GymExercisesDtoItem?) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.MEDIUM_PADDING, vertical = SMALL_PADDING),
        onClick = {
            navigateToExerciseDetailsScreen(exerciseEntity.exerciseDetails)
        },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.cardBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(9f)
                    .background(ColorsUtil.cardBackgroundColor)
                    .padding(Dimensions.MEDIUM_PADDING)
            ) {

                exerciseEntity.exerciseDetails?.let { exercise ->
                    Text(
                        text = exercise.name,
                        color = ColorsUtil.primaryTextColor,
                        fontSize = 20.nonScaledSp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(SMALL_PADDING))

                    val hour12Format = exerciseEntity.hour % 12
                    val amPm = if (exerciseEntity.hour < 12) "AM" else "PM"
                    val formattedTime = LocalTime.of(hour12Format, exerciseEntity.minutes)
                        .format(DateTimeFormatter.ofPattern("hh: mm"))

                    Text(
                        buildAnnotatedString {
                            append("Performed at ")
                            withStyle(
                                style = SpanStyle(
                                    color = ColorsUtil.primaryTextColor,
                                )
                            ) {
                                append("$formattedTime $amPm")
                            }
                        },
                        color = ColorsUtil.primaryTextColor,
                        fontSize = 13.nonScaledSp
                    )

                    Spacer(modifier = Modifier.height(SMALL_PADDING))
                }
            }

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = ColorsUtil.noAchievementColor,
                modifier = Modifier
                    .padding(SMALL_PADDING)
                    .clickable {
                        removeExercise(exerciseEntity)
                    }
            )
        }
    }
}

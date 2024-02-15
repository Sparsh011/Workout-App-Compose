package com.sparshchadha.workout_app.ui.screens.workout.gym

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.ui.components.CalendarRow
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GymExercisesPerformed(
    navController: NavHostController,
    exercisesPerformed: List<GymExercisesEntity>?,
    globalPaddingValues: PaddingValues,
    uiEventState: State<WorkoutViewModel.UIEvent?>,
    selectedDateAndMonth: Pair<Int, String>?,
    getExercisesPerformedOn: (Pair<Int, String>) -> Unit,
) {
    uiEventState.value?.let { event ->
        HandleUIEventsForExercisesPerformedToday(
            event = event,
            exercisesPerformed = exercisesPerformed,
            globalPaddingValues = globalPaddingValues,
            navController = navController,
            selectedDateAndMonth = selectedDateAndMonth,
            getExercisesPerformedOn = getExercisesPerformedOn
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HandleUIEventsForExercisesPerformedToday(
    event: WorkoutViewModel.UIEvent,
    globalPaddingValues: PaddingValues,
    exercisesPerformed: List<GymExercisesEntity>?,
    navController: NavHostController,
    selectedDateAndMonth: Pair<Int, String>?,
    getExercisesPerformedOn: (Pair<Int, String>) -> Unit,
) {
    when (event) {
        is WorkoutViewModel.UIEvent.ShowLoader -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gym_exercises_animation))
            val progress by animateLottieCompositionAsState(composition)
            ShowLoadingScreen(
                composition = composition,
                progress = progress
            )
        }

        is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
            PopulatePerformedExercises(
                globalPaddingValues = globalPaddingValues,
                exercisesPerformed = exercisesPerformed,
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = BottomBarScreen.WorkoutScreen.route,
                        inclusive = false
                    )
                },
                selectedDateAndMonth = selectedDateAndMonth,
                getExercisesPerformedOn = getExercisesPerformedOn
            )
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
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
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Exercises Performed Today",
                onBackButtonPressed = onBackButtonPressed
            )
        },
        containerColor = Color.White
    ) { localPaddingValues ->

        LazyColumn(
            modifier = Modifier
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
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_gym_or_yoga_performed))
                    val progress by animateLottieCompositionAsState(composition)

                    NoWorkoutPerformedOrFoodConsumed(
                        text = "No Exercise Performed!",
                        composition = composition,
                        progress = progress,
                        localPaddingValues = localPaddingValues,
                        animationModifier = Modifier.size(Dimensions.LOTTIE_ANIMATION_SIZE_LARGE)
                    )
                }
            } else {
                items(exercisesPerformed) {
                    ExerciseEntity(exerciseEntity = it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEntity(
    exerciseEntity: GymExercisesEntity,
) {
    var shouldShowExerciseDetailsBottomSheet by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM_PADDING),
        onClick = {
            shouldShowExerciseDetailsBottomSheet = true
        },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryLightGray
        )
    ) {
        Column(
            modifier = Modifier
                .background(ColorsUtil.primaryLightGray)
                .padding(Dimensions.LARGE_PADDING)
        ) {

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            exerciseEntity.exerciseDetails?.let { exercise ->
                Text(
                    text = exercise.name,
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 26.nonScaledSp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

                Text(
                    buildAnnotatedString {
                        append("Difficulty Level : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryDarkTextColor,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(exercise.difficulty)
                        }
                    },
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 16.nonScaledSp
                )

                Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

                Text(
                    buildAnnotatedString {
                        append("Sets Performed : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryDarkTextColor,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(exerciseEntity.setsPerformed.toString())
                        }
                    },
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 16.nonScaledSp
                )

                Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))
            }
        }
    }

    if (shouldShowExerciseDetailsBottomSheet) {
        exerciseEntity.exerciseDetails?.let {
            ExerciseDetailsModalBottomSheet(
                exercise = it,
                hideBottomSheet = {
                    shouldShowExerciseDetailsBottomSheet = false
                }
            )
        }
    }
}

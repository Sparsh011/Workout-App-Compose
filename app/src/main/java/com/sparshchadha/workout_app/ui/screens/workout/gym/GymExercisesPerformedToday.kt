package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.entities.GymExercisesEntity
import com.sparshchadha.workout_app.ui.components.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun GymExercisesPerformedToday(
    navController: NavHostController,
    exercisesPerformed: List<GymExercisesEntity>?,
    globalPaddingValues: PaddingValues,
    uiEventState: State<WorkoutViewModel.UIEvent?>,
) {
    uiEventState.value?.let { event ->
        HandleUIEventsForExercisesPerformedToday(
            event = event,
            exercisesPerformed = exercisesPerformed,
            globalPaddingValues = globalPaddingValues,
            navController = navController
        )
    }
}

@Composable
fun HandleUIEventsForExercisesPerformedToday(
    event: WorkoutViewModel.UIEvent,
    globalPaddingValues: PaddingValues,
    exercisesPerformed: List<GymExercisesEntity>?,
    navController: NavHostController,
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
                }
            )
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
}

@Composable
fun PopulatePerformedExercises(
    globalPaddingValues: PaddingValues,
    exercisesPerformed: List<GymExercisesEntity>?,
    onBackButtonPressed: () -> Unit,
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
        if (exercisesPerformed.isNullOrEmpty()) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_results_found_animation))
            val progress by animateLottieCompositionAsState(composition)

            NoWorkoutPerformedOrFoodConsumed(
                text = "No Exercise Performed Yet!",
                composition = composition,
                progress = progress,
                localPaddingValues = localPaddingValues
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding()
                    )
            ) {
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
            .padding(10.dp),
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
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            exerciseEntity.exerciseDetails?.let { exercise ->
                Text(
                    text = exercise.name,
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    buildAnnotatedString {
                        append("Difficulty Level : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryDarkTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append(exercise.difficulty)
                        }
                    },
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append("Sets Performed : ")
                        withStyle(
                            style = SpanStyle(
                                color = ColorsUtil.primaryDarkTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append(exerciseEntity.setsPerformed.toString())
                        }
                    },
                    color = ColorsUtil.primaryDarkTextColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
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

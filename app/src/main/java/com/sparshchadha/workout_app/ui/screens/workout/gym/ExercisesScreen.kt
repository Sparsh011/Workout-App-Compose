package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.ui.components.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ShowLoadingScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun ExercisesScreen(
    navController: NavController,
    category: String?,
    exercises: GymWorkoutsDto?,
    uiEventState: WorkoutViewModel.UIEvent?,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gym_exercises_animation))
    val progress by animateLottieCompositionAsState(composition)

    uiEventState?.let { event ->
        when (event) {
            is WorkoutViewModel.UIEvent.ShowLoader -> {
                ShowLoadingScreen(
                    composition = composition,
                    progress = progress
                )
            }

            is WorkoutViewModel.UIEvent.HideLoader -> {
                ShowExercises(
                    category = category,
                    navController = navController,
                    exercises = exercises,
                    composition = composition,
                    progress = progress
                )
            }

            is WorkoutViewModel.UIEvent.ShowError -> {
                ErrorDuringFetch(errorMessage = event.errorMessage)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowExercises(
    category: String?,
    navController: NavController,
    exercises: GymWorkoutsDto?,
    composition: LottieComposition?,
    progress: Float,
) {
    Scaffold(
        topBar = {
            val topBarDescription = category ?: "Exercises"
            ScaffoldTopBar(topBarDescription = topBarDescription, onBackButtonPressed = {
                navController.popBackStack(route = UtilityScreen.SelectExerciseCategory.route, inclusive = false)
            })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (exercises != null) {
                items(exercises) {
                    var shouldShowBottomSheet by remember {
                        mutableStateOf(false)
                    }

                    Exercise(
                        name = it.name,
                        difficulty = it.difficulty,
                        equipment = it.equipment,
                        muscle = it.muscle,
                        instructions = it.instructions,
                        showBottomSheet = {
                            shouldShowBottomSheet = true
                        },
                        hideBottomSheet = {
                            shouldShowBottomSheet = false
                        },
                        shouldShowBottomSheet = shouldShowBottomSheet,
                        sheetState = rememberModalBottomSheetState()
                    )
                }
            } else {
                item {
                    LottieAnimation(composition = composition, progress = { progress })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise(
    name: String,
    difficulty: String,
    equipment: String,
    muscle: String,
    instructions: String,
    showBottomSheet: () -> Unit,
    hideBottomSheet: () -> Unit,
    shouldShowBottomSheet: Boolean,
    sheetState: SheetState,
) {
    if (shouldShowBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                hideBottomSheet()
            },
            windowInsets = WindowInsets(0, 0, 0, 10),
            containerColor = Color.White,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ExerciseSubTitlesAndDescription(subTitle = "Exercise Name", description = name)
                ExerciseSubTitlesAndDescription(subTitle = "Difficulty", description = difficulty)
                ExerciseSubTitlesAndDescription(subTitle = "Muscle", description = muscle)
                ExerciseSubTitlesAndDescription(subTitle = "Equipment Required", description = equipment)
                ExerciseSubTitlesAndDescription(subTitle = "Instructions", description = instructions)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                showBottomSheet()
            },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryLightGray
        )
    ) {
        ExerciseSubTitlesAndDescription(subTitle = "Exercise Name", description = name)
        ExerciseSubTitlesAndDescription(subTitle = "Difficulty", description = difficulty)
        ExerciseSubTitlesAndDescription(subTitle = "Muscle", description = muscle)
    }
}

@Composable
fun ExerciseSubTitlesAndDescription(subTitle: String, description: String) {
    Text(
        text = subTitle,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )

    Text(
        text = description,
        modifier = Modifier.padding(10.dp),
        color = ColorsUtil.primaryDarkTextColor
    )
}
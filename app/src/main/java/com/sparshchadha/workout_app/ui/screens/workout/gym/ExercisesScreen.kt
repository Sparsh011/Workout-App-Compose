package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun ExercisesScreen(
    navController: NavController,
    category: String?,
    exercises: GymExercisesDto?,
    uiEventState: WorkoutViewModel.UIEvent?,
    globalPaddingValues: PaddingValues,
    saveExercise: (GymExercisesEntity) -> Unit,
    workoutViewModel: WorkoutViewModel,
) {

    uiEventState?.let { event ->
        Scaffold(
            topBar = {
                val topBarDescription = category ?: "Exercises"
                ScaffoldTopBar(
                    topBarDescription = topBarDescription,
                    onBackButtonPressed = {
                        navController.popBackStack(
                            route = UtilityScreen.SelectExerciseCategory.route,
                            inclusive = false
                        )
                    }
                )
            }
        ) { localPaddingValues ->
            HandleUIEventsForExercises(
                event = event,
                category = category,
                navController = navController,
                exercises = exercises,
                globalPaddingValues = globalPaddingValues,
                saveExercise = saveExercise,
                workoutViewModel = workoutViewModel,
                localPaddingValues = localPaddingValues
            )
        }
    }
}

@Composable
fun HandleUIEventsForExercises(
    event: WorkoutViewModel.UIEvent,
    category: String?,
    navController: NavController,
    exercises: GymExercisesDto?,
    globalPaddingValues: PaddingValues,
    saveExercise: (GymExercisesEntity) -> Unit,
    workoutViewModel: WorkoutViewModel,
    localPaddingValues: PaddingValues,
) {
    when (event) {
        is WorkoutViewModel.UIEvent.ShowLoader -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
            val progress by animateLottieCompositionAsState(composition)
            ShowLoadingScreen(
                composition = composition,
                progress = progress
            )
        }

        is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
            val progress by animateLottieCompositionAsState(composition)
            ShowExercises(
                category = category,
                navController = navController,
                exercises = exercises,
                composition = composition,
                progress = progress,
                globalPaddingValues = globalPaddingValues,
                saveExercise = saveExercise,
                workoutViewModel = workoutViewModel,
                localPaddingValues
            )
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
}

@Composable
fun ShowExercises(
    category: String?,
    navController: NavController,
    exercises: GymExercisesDto?,
    composition: LottieComposition?,
    progress: Float,
    globalPaddingValues: PaddingValues,
    saveExercise: (GymExercisesEntity) -> Unit,
    workoutViewModel: WorkoutViewModel,
    localPaddingValues: PaddingValues,
) {

    LazyColumn(
        modifier = Modifier
            .background(scaffoldBackgroundColor)
            .padding(
                top = localPaddingValues.calculateTopPadding(),
                bottom = globalPaddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
    ) {
        if (exercises != null) {
            items(exercises) { exercise ->
                var shouldShowBottomSheet by remember {
                    mutableStateOf(false)
                }

                Exercise(
                    hideBottomSheet = {
                        shouldShowBottomSheet = false
                    },
                    shouldShowBottomSheet = shouldShowBottomSheet,
                    exercise = exercise,
                    saveExercise = saveExercise
                ) {
                    workoutViewModel.updateExerciseDetails(it)
                    navController.navigate(UtilityScreen.ExerciseDetailScreen.route)
                }
            }
        } else {
            item {
                LottieAnimation(composition = composition, progress = { progress })
            }
        }
    }
}

@Composable
fun Exercise(
    hideBottomSheet: () -> Unit,
    shouldShowBottomSheet: Boolean,
    exercise: GymExercisesDtoItem,
    saveExercise: (GymExercisesEntity) -> Unit,
    navigateToExerciseDetailsScreen: (GymExercisesDtoItem) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToExerciseDetailsScreen(exercise)
            }
            .padding(MEDIUM_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(4f)
        ) {
            ExerciseSubTitlesAndDescription(
                subTitle = "Exercise Name",
                description = exercise.name,
                showSubtitle = false
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.weight(1f),
            tint = primaryTextColor
        )
    }

    CustomDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsModalBottomSheet(
    exercise: GymExercisesDtoItem,
    hideBottomSheet: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        },
        windowInsets = WindowInsets(0, 0, 0, 10),
        containerColor = Color.White,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            ExerciseSubTitlesAndDescription(subTitle = "Exercise Name", description = exercise.name)
            ExerciseSubTitlesAndDescription(
                subTitle = "Difficulty",
                description = exercise.difficulty
            )
            ExerciseSubTitlesAndDescription(subTitle = "Muscle", description = exercise.muscle)
            ExerciseSubTitlesAndDescription(
                subTitle = "Equipment Required",
                description = exercise.equipment
            )
            ExerciseSubTitlesAndDescription(
                subTitle = "Instructions",
                description = exercise.instructions
            )
        }
    }
}

@Composable
fun ExerciseSubTitlesAndDescription(
    subTitle: String,
    description: String,
    showSubtitle: Boolean = true,
) {
    if (showSubtitle) {
        Text(
            text = subTitle, // already capitalized when passed as argument
            fontWeight = FontWeight.Bold,
            fontSize = 20.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
        )

        Text(
            text = description.capitalize(),
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING),
            color = primaryTextColor,
            fontSize = 14.nonScaledSp
        )
    } else {
        Text(
            text = description.capitalize(),
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING),
            color = primaryTextColor,
            fontSize = 18.nonScaledSp
        )
    }
}
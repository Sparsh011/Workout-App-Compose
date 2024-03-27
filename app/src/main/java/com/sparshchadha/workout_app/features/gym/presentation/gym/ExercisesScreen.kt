package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.features.gym.data.remote.dto.gym_workout.GymExercisesDtoItem
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.shared_ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.shared_ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.Resource
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

@Composable
fun ExercisesScreen(
    navController: NavController,
    category: String?,
    globalPaddingValues: PaddingValues,
    workoutViewModel: WorkoutViewModel,
) {

    val exercises by workoutViewModel.gymExercisesFromApi

    Scaffold(
        topBar = {
            val topBarDescription = category ?: "Exercises"
            ScaffoldTopBar(
                topBarDescription = topBarDescription,
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = UtilityScreenRoutes.SelectExerciseCategory.route,
                        inclusive = false
                    )
                }
            )
        },
        modifier = Modifier
            .padding(
                bottom = globalPaddingValues.calculateBottomPadding()
            )
            .fillMaxSize(),
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->
        HandleExercises(
            navController = navController,
            exercises = exercises,
            workoutViewModel = workoutViewModel,
            localPaddingValues = localPaddingValues
        )
    }

}

@Composable
fun HandleExercises(
    navController: NavController,
    exercises: Resource<GymExercisesDto>?,
    workoutViewModel: WorkoutViewModel,
    localPaddingValues: PaddingValues,
) {
    when (exercises) {
        is Resource.Loading -> {
            ShowLoadingScreen()
        }

        is Resource.Success -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
            val progress by animateLottieCompositionAsState(composition)
            ShowExercises(
                navController = navController,
                exercises = exercises.data,
                composition = composition,
                progress = progress,
                updateExerciseDetails = {
                    workoutViewModel.updateExerciseDetails(it)
                },
                localPaddingValues = localPaddingValues
            )
        }

        is Resource.Error -> {
            ErrorDuringFetch(errorMessage = exercises.error?.message ?: "Unable To Get Result")
        }

        else -> {}
    }
}

@Composable
fun ShowExercises(
    navController: NavController,
    exercises: GymExercisesDto?,
    composition: LottieComposition?,
    progress: Float,
    updateExerciseDetails: (GymExercisesDtoItem) -> Unit,
    localPaddingValues: PaddingValues,
) {

    LazyColumn(
        modifier = Modifier
            .padding(
                top = localPaddingValues.calculateTopPadding() + MEDIUM_PADDING,
                bottom = MEDIUM_PADDING,
                start = MEDIUM_PADDING,
                end = MEDIUM_PADDING
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(MEDIUM_PADDING))
            .background(bottomBarColor)
    ) {
        if (exercises != null) {
            items(exercises) { exercise ->
                Exercise(
                    exercise = exercise,
                    navigateToExerciseDetailsScreen = {
                        updateExerciseDetails(it)
                        navController.navigate(UtilityScreenRoutes.ExerciseDetailScreen.route)
                    }
                )
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
    exercise: GymExercisesDtoItem,
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
                title = exercise.name,
                description = "Requires " + exercise.equipment,
                showDescription = true,
                descriptionColor = targetAchievedColor
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.weight(1f),
            tint = primaryPurple
        )
    }
}

@Composable
fun ExerciseSubTitlesAndDescription(
    title: String,
    description: String,
    showDescription: Boolean = true,
    descriptionColor: Color = primaryTextColor
) {
    if (showDescription) {
        Text(
            text = title, // already capitalized when passed as argument
            fontWeight = FontWeight.Bold,
            fontSize = 20.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
        )

        Text(
            text = description.capitalize(),
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING),
            color = descriptionColor,
            fontSize = 14.nonScaledSp
        )
    } else {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING),
            color = primaryTextColor,
            fontSize = 20.nonScaledSp
        )
    }
}
package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun SelectExerciseCategory(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    globalPaddingValues: PaddingValues,
) {
    // TODO - clean this code after navigation is cleaned when passing args
    when (workoutViewModel.getCurrentCategoryTypeForGymWorkout()) {

        CategoryType.DIFFICULTY_LEVEL -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getDifficultyLevels(),
                topBarDescription = "Select Difficulty Level",
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByDifficultyFromApi(difficultyLevel = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(BottomBarScreen.WorkoutScreen.route, inclusive = false)
                }
            )
        }

        CategoryType.WORKOUT_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getWorkoutTypes(),
                topBarDescription = "Select A Program",
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByWorkoutTypeFromApi(workoutType = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(BottomBarScreen.WorkoutScreen.route, inclusive = false)
                }
            )
        }

        CategoryType.MUSCLE_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getMuscleTypes(),
                topBarDescription = "Select Body Part To Train",
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByMuscleFromApi(muscleType = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(BottomBarScreen.WorkoutScreen.route, inclusive = false)
                }
            )
        }

        CategoryType.SEARCH_EXERCISE -> {
            // Handled in WorkoutScreen
        }
    }
}

@Composable
fun ExerciseCategoryItems(
    categoryItems: List<String>,
    topBarDescription: String,
    globalPaddingValues: PaddingValues,
    onCategoryItemSelected: (String) -> Unit,
    onBackButtonPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = topBarDescription,
                onBackButtonPressed = onBackButtonPressed
            )
        },
        containerColor = Color.White
    ) { localPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(top = localPaddingValues.calculateTopPadding(), bottom = globalPaddingValues.calculateBottomPadding())
        ) {

            items(categoryItems) { categoryItem ->
                CategoryItem(
                    categoryItem = categoryItem,
                    onCategoryItemSelected = onCategoryItemSelected
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    categoryItem: String,
    onCategoryItemSelected: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCategoryItemSelected(categoryItem)
        }
    ) {
        Text(
            text = categoryItem,
            fontSize = 20.sp,
            color = ColorsUtil.primaryDarkTextColor,
            modifier = Modifier
                .padding(20.dp)
                .weight(0.8f)
                .fillMaxWidth()
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.primaryDarkTextColor,
            modifier = Modifier.padding(20.dp)
        )
    }

    CustomDivider()
}
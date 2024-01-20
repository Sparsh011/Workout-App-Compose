package com.sparshchadha.workout_app.ui.screens.workout.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun SelectExerciseCategory(
    workoutViewModel: WorkoutViewModel,
    navController: NavController
) {

    when (workoutViewModel.getCurrentCategoryTypeForGymWorkout()) {
        CategoryType.DIFFICULTY_LEVEL -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getDifficultyLevels(),
                topBarDescription = "Select Difficulty Level",
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByDifficulty(difficultyLevel = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                }
            )
        }

        CategoryType.WORKOUT_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getWorkoutTypes(),
                topBarDescription = "Select A Program",
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByWorkoutType(workoutType = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                }
            )
        }

        CategoryType.MUSCLE_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getMuscleTypes(),
                topBarDescription = "Select Body Part To Train",
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByMuscle(muscleType = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                }
            )
        }

        CategoryType.SEARCH_EXERCISE -> {
            // Handled in WorkoutScreen
        }
    }
}

@Composable
fun ExerciseCategoryItems(categoryItems: List<String>, topBarDescription: String, onCategoryItemSelected: (String) -> Unit) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 10.dp)
                )

                Text(
                    text = topBarDescription,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(paddingValues = it)
        ) {

            items(categoryItems) { categoryItem ->
                CategoryItemComposable(
                    categoryItem = categoryItem,
                    onCategoryItemSelected = onCategoryItemSelected
                )
            }
        }
    }
}

@Composable
fun CategoryItemComposable(categoryItem: String, onCategoryItemSelected: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCategoryItemSelected(categoryItem)
        }
    ) {
        Text(
            text = categoryItem,
            fontSize = 20.sp,
            color = ColorsUtil.primaryBackgroundColor,
            modifier = Modifier
                .padding(20.dp)
                .weight(0.8f)
                .fillMaxWidth()
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = ColorsUtil.primaryBackgroundColor,
            modifier = Modifier.padding(20.dp)
        )
    }

    Divider(
        color = ColorsUtil.primaryLightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    )
}
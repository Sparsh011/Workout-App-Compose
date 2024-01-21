package com.sparshchadha.workout_app.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.CategoryType
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun WorkoutScreenComposable(
    difficultyLevels: List<DifficultyLevel>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    gymWorkoutCategories: List<String>,
) {

    WorkoutScreen(
        difficultyLevels = difficultyLevels,
        workoutViewModel = workoutViewModel,
        navController = navController,
        gymWorkoutCategories = gymWorkoutCategories
    )
}


@Composable
fun WorkoutScreen(
    difficultyLevels: List<DifficultyLevel>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    gymWorkoutCategories: List<String>,
) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "What Would You Like To Do Today?",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(0.9f)
                )
            }
        }

        // Gym workout
        header {
            Text(
                text = "Gym Workout",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

        items(gymWorkoutCategories) {
           PopulateWorkoutCategories(category = it, modifier = Modifier.padding(20.dp), onCategorySelection = { categorySelected ->
               when(categorySelected) {
                   "Program" -> {
                       workoutViewModel.updateCategoryTypeForGymWorkout(
                           categoryType = CategoryType.WORKOUT_TYPE
                       )
                       navController.navigate(UtilityScreen.SelectExerciseCategory.route)
                   }

                   "Body Part" -> {
                       workoutViewModel.updateCategoryTypeForGymWorkout(
                           categoryType = CategoryType.MUSCLE_TYPE
                       )
                       navController.navigate(UtilityScreen.SelectExerciseCategory.route)
                   }

                   "Difficulty" -> {
                       workoutViewModel.updateCategoryTypeForGymWorkout(
                           categoryType = CategoryType.DIFFICULTY_LEVEL
                       )
                       navController.navigate(UtilityScreen.SelectExerciseCategory.route)
                   }

                   "Search Exercise" -> {
                       workoutViewModel.updateCategoryTypeForGymWorkout(
                           categoryType = CategoryType.SEARCH_EXERCISE
                       )
                       navController.navigate("SearchScreen/exercises")
                   }
               }
           })
        }

        // Yoga poses
        header {
            Text(
                text = "Yoga Poses",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

        items(difficultyLevels) {
            PopulateYogaDifficulty(
                modifier = Modifier.padding(all = 20.dp),
                yogaDifficulty = it,
                onYogaDifficultySelection = { difficultyLevel ->
                    workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                    // navigate to yoga screen
                    workoutViewModel.getYogaPoses()
                    navController.navigate(route = UtilityScreen.YogaPoses.route)
                },
                textColor = Color.Black
            )
        }

        header {
            Text(
                text = "Track Your Today's Workout",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

    }
}

@Composable
fun PopulateWorkoutCategories(
    category: String,
    modifier: Modifier,
    onCategorySelection: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(all = 20.dp)
            .clickable {
                onCategorySelection(category)
            },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryFoodCardBackground
        )
    ) {
        Text(
            text = category,
            color = ColorsUtil.textColor,
            modifier = modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PopulateYogaDifficulty(
    yogaDifficulty: DifficultyLevel,
    modifier: Modifier,
    textColor: Color,
    onYogaDifficultySelection: (DifficultyLevel) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(all = 20.dp)
            .clickable {
                onYogaDifficultySelection(yogaDifficulty)
            },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryFoodCardBackground
        )
    ) {
        Text(
            text = yogaDifficulty.name.lowercase().capitalize(),
            color = ColorsUtil.textColor,
            modifier = modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit,
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
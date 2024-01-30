package com.sparshchadha.workout_app.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel


@Composable
fun GymAndYogaWorkoutHomeScreen(
    difficultyLevels: List<DifficultyLevel>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    gymWorkoutCategories: List<String>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {

        header {
            Text(
                text = "What Would You Like To Do Today?",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(all = 10.dp)

            )
        }

        // Gym workout
        header {
            HeaderText(
                heading = "Gym Workout"
            )
        }

        items(gymWorkoutCategories) {
            PopulateWorkoutCategories(
                category = it,
                modifier = Modifier.padding(20.dp),
                onCategorySelection = { categorySelected ->
                    handleGymExercisesCategorySelection(
                        categorySelected = categorySelected,
                        updateGymWorkoutCategory = { categoryType ->
                            workoutViewModel.updateCategoryTypeForGymWorkout(categoryType = categoryType)
                        },
                        navigateToScreen = { route ->
                            navController.navigate(route)
                        },
                    )
                }
            )
        }

        // Yoga poses
        header {
            HeaderText(
                heading = "Yoga Poses"
            )
        }

        items(difficultyLevels) {
            PopulateYogaDifficulty(
                yogaDifficulty = it,
                modifier = Modifier.padding(all = 20.dp)
            ) { difficultyLevel ->
                workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                // navigate to yoga screen
                workoutViewModel.getYogaPosesFromApi()
                navController.navigate(route = UtilityScreen.YogaPoses.route)
            }
        }

        header {
            HeaderText(
                heading = "Track Today's Workout"
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) }
        ) {
            TodayWorkoutCard(
                category = "Yoga",
                onCategoryItemSelected = {
                    navController.navigate(UtilityScreen.YogaPosesPerformedToday.route)
                }
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) }
        ) {
            TodayWorkoutCard(
                category = "Gym",
                onCategoryItemSelected = {
                    navController.navigate(UtilityScreen.GymExercisesPerformedToday.route)
                }
            )
        }
    }
}

@Composable
fun TodayWorkoutCard(
    category: String,
    onCategoryItemSelected: () -> Unit,
) {

    Card (
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryGreenCardBackground
        ),
        modifier = Modifier.padding(10.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onCategoryItemSelected()
            }
        ) {
            Text(
                text = category,
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
    }
}

@Composable
fun HeaderText(heading: String) {
    Text(
        text = heading,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = Modifier.padding(all = 10.dp)
    )
}

fun handleGymExercisesCategorySelection(
    categorySelected: String,
    updateGymWorkoutCategory: (CategoryType) -> Unit,
    navigateToScreen: (String) -> Unit,
) {
    when (categorySelected) {
        "Program" -> {
            updateGymWorkoutCategory(CategoryType.WORKOUT_TYPE)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        "Body Part" -> {
            updateGymWorkoutCategory(CategoryType.MUSCLE_TYPE)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        "Difficulty" -> {
            updateGymWorkoutCategory(CategoryType.DIFFICULTY_LEVEL)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        "Search Exercise" -> {
            updateGymWorkoutCategory(CategoryType.SEARCH_EXERCISE)
            navigateToScreen("SearchScreen/exercises")
        }
    }
}

@Composable
fun PopulateWorkoutCategories(
    category: String,
    modifier: Modifier,
    onCategorySelection: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(all = 20.dp)
            .clickable {
                onCategorySelection(category)
            },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryGreenCardBackground
        )
    ) {
        Text(
            text = category,
            color = ColorsUtil.primaryDarkTextColor,
            modifier = modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PopulateYogaDifficulty(
    yogaDifficulty: DifficultyLevel,
    modifier: Modifier,
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
            containerColor = ColorsUtil.primaryGreenCardBackground
        )
    ) {
        Text(
            text = yogaDifficulty.name.lowercase().capitalize(),
            color = ColorsUtil.primaryDarkTextColor,
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
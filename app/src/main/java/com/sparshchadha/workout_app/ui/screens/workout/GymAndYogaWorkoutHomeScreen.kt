package com.sparshchadha.workout_app.ui.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun GymAndYogaWorkoutHomeScreen(
    difficultyLevels: List<DifficultyLevel>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    gymWorkoutCategories: List<String>,
    globalPaddingValues: PaddingValues,
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(topBarDescription = "What Would You Like To Do Today?", showBackIcon = false)
        }
    ) { localPaddingValues ->

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(
                    top = localPaddingValues.calculateTopPadding(),
                    start = 20.dp,
                    end = 20.dp,
                    bottom = globalPaddingValues.calculateBottomPadding()
                )
        ) {

            // Gym workout
            header {
                HeaderText(
                    heading = "Gym Workout"
                )
            }

            header {
                AsyncImage(
                    model = R.drawable.dumbbell_svg,
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(10.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
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

            header {
                AsyncImage(
                    model = R.drawable.yoga_svg,
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(10.dp)
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
                    heading = "Track Workouts"
                )
            }

            header {
                TodayWorkoutCard(
                    category = "Yoga",
                    onCategoryItemSelected = {
                        navController.navigate(UtilityScreen.YogaPosesPerformed.route)
                    },
                    icon = R.drawable.yoga_svg
                )
            }

            header {
                TodayWorkoutCard(
                    category = "Gym",
                    onCategoryItemSelected = {
                        navController.navigate(UtilityScreen.GymExercisesPerformed.route)
                    },
                    icon = R.drawable.dumbbell_svg,
                    showDivider = false
                )
            }
        }
    }

}

@Composable
fun TodayWorkoutCard(
    category: String,
    onCategoryItemSelected: () -> Unit,
    icon: Int,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    onCategoryItemSelected()
                },
        ) {
            AsyncImage(
                model = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(30.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )

            Text(
                text = category,
                fontSize = 20.sp,
                color = ColorsUtil.primaryDarkTextColor,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.8f)
                    .fillMaxWidth()
            )

            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    ColorsUtil.primaryDarkTextColor
                ),
                modifier = Modifier.padding(20.dp)
            )
        }

        if (showDivider) CustomDivider(dividerColor = ColorsUtil.primaryLightGray)
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
        GymWorkoutCategories.PROGRAM.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.WORKOUT_TYPE)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.MUSCLE.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.MUSCLE_TYPE)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.DIFFICULTY.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.DIFFICULTY_LEVEL)
            navigateToScreen(UtilityScreen.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.SEARCH.name.lowercase().capitalize() -> {
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
            containerColor = ColorsUtil.primaryLightGray
        )
    ) {
        Text(
            text = category,
            color = ColorsUtil.primaryDarkTextColor,
            modifier = modifier.align(CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 15.nonScaledSp
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
            containerColor = ColorsUtil.primaryLightGray
        )
    ) {
        Text(
            text = yogaDifficulty.name.lowercase().capitalize(),
            color = ColorsUtil.primaryDarkTextColor,
            modifier = modifier.align(CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 15.nonScaledSp
        )
    }
}

fun LazyStaggeredGridScope.header(
    content: @Composable LazyStaggeredGridItemScope.() -> Unit,
) {
    item(content = content, span = StaggeredGridItemSpan.FullLine)
}
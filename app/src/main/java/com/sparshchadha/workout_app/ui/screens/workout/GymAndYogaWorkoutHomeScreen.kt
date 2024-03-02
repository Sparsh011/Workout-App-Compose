package com.sparshchadha.workout_app.ui.screens.workout

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.gym.CategoryItem
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Dimensions.YOGA_AND_DUMBBELL_SVG_SIZE
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GymAndYogaWorkoutHomeScreen(
    difficultyLevels: List<DifficultyLevel>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    gymWorkoutCategories: List<String>,
    globalPaddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBackgroundColor)
    ) {
        Text(
            text = "Perform Workout",
            fontSize = 20.nonScaledSp,
            modifier = Modifier
                .fillMaxWidth()
                .background(statusBarColor)
                .padding(vertical = SMALL_PADDING, horizontal = MEDIUM_PADDING),
            color = Color.White
        )

        LazyColumn(
            modifier = Modifier
                .background(scaffoldBackgroundColor)
                .padding(
                    top = SMALL_PADDING,
                    start = MEDIUM_PADDING,
                    end = MEDIUM_PADDING,
                    bottom = globalPaddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {

            // Gym workout
            stickyHeader {
                Surface(
                    modifier = Modifier.fillParentMaxWidth(),
                    color = scaffoldBackgroundColor
                ) {
                    HeaderText(
                        heading = "Gym Workout"
                    )
                }
            }

            items(gymWorkoutCategories.size) {
                CategoryItem(
                    categoryItem = gymWorkoutCategories[it],
                    onCategoryItemSelected = { categorySelected ->
                        handleGymExercisesCategorySelection(
                            categorySelected = categorySelected,
                            updateGymWorkoutCategory = { categoryType ->
                                workoutViewModel.updateCategoryTypeForGymWorkout(categoryType = categoryType)
                            },
                            navigateToScreen = { route ->
                                navController.navigate(route)
                            },
                        )
                    },
                    showDivider = it != gymWorkoutCategories.size - 1
                )
            }

            // Yoga poses
            stickyHeader {
                Surface(
                    modifier = Modifier.fillParentMaxWidth(),
                    color = scaffoldBackgroundColor
                ) {
                    HeaderText(
                        heading = "Yoga Poses"
                    )
                }
            }

            items(difficultyLevels.size) {
                PopulateYogaDifficulty(
                    yogaDifficulty = difficultyLevels[it],
                    onYogaDifficultySelection = { difficultyLevel ->
                        workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                        // navigate to yoga screen
                        workoutViewModel.getYogaPosesFromApi()
                        navController.navigate(route = UtilityScreen.YogaPoses.route)
                    },
                    showDivider = it != difficultyLevels.size - 1
                )
            }

            stickyHeader {
                HeaderText(
                    heading = "Track Workouts"
                )
            }

            item {
                TodayWorkoutCard(
                    category = "Yoga",
                    onCategoryItemSelected = {
                        navController.navigate(UtilityScreen.YogaPosesPerformed.route)
                    },
                    icon = R.drawable.yoga_svg
                )
            }

            item {
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
                .padding(horizontal = SMALL_PADDING)
                .clickable {
                    onCategoryItemSelected()
                },
        ) {
            AsyncImage(
                model = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(MEDIUM_PADDING)
                    .size(YOGA_AND_DUMBBELL_SVG_SIZE),
                colorFilter = ColorFilter.tint(primaryTextColor)
            )

            Text(
                text = category,
                fontSize = 16.nonScaledSp,
                color = primaryTextColor,
                modifier = Modifier
                    .padding(MEDIUM_PADDING)
                    .weight(0.8f)
                    .fillMaxWidth()
            )

            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    primaryTextColor
                ),
                modifier = Modifier.padding(horizontal = LARGE_PADDING)
            )
        }

        if (showDivider) CustomDivider(dividerColor = ColorsUtil.dividerColor, verticalPadding = 0.dp)
    }
}

@Composable
fun HeaderText(
    heading: String,
) {
    Text(
        text = heading,
        fontSize = 20.nonScaledSp,
        color = primaryTextColor,
        modifier = Modifier
            .padding(vertical = MEDIUM_PADDING, horizontal = SMALL_PADDING)
            .fillMaxWidth(),
        fontWeight = FontWeight.Bold
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
            color = primaryTextColor,
            modifier = modifier.align(CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 15.nonScaledSp
        )
    }
}

@Composable
fun PopulateYogaDifficulty(
    yogaDifficulty: DifficultyLevel,
    onYogaDifficultySelection: (DifficultyLevel) -> Unit,
    showDivider: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onYogaDifficultySelection(yogaDifficulty)
        }
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
    ) {
        Text(
            text = yogaDifficulty.name.lowercase().capitalize(),
            fontSize = 16.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(0.8f)
                .fillMaxWidth()
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = primaryTextColor,
            modifier = Modifier.padding(vertical = MEDIUM_PADDING)
        )
    }

    if (showDivider) {
        CustomDivider()
    }
}
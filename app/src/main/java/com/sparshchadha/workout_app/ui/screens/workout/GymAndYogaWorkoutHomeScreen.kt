package com.sparshchadha.workout_app.ui.screens.workout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.screens.workout.gym.CategoryItem
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.CategoryType
import com.sparshchadha.workout_app.ui.screens.workout.gym.util.GymWorkoutCategories
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
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
                .padding(
                    start = MEDIUM_PADDING,
                    end = SMALL_PADDING,
                    top = SMALL_PADDING,
                    bottom = MEDIUM_PADDING
                ),
            color = Color.White
        )

        Column(
            modifier = Modifier
                .background(scaffoldBackgroundColor)
                .padding(
                    top = SMALL_PADDING,
                    start = MEDIUM_PADDING,
                    end = MEDIUM_PADDING,
                    bottom = globalPaddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Gym workout
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scaffoldBackgroundColor
            ) {
                HeaderText(
                    heading = "Gym Workout"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(bottomBarColor)
            ) {
                for (it in gymWorkoutCategories.indices) {
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
                        showDivider = false
                    )
                }
            }

            // Yoga poses
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = scaffoldBackgroundColor
            ) {
                HeaderText(
                    heading = "Yoga Poses"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(bottomBarColor)
            ) {
                for (it in difficultyLevels.indices) {
                    PopulateYogaDifficulty(
                        yogaDifficulty = difficultyLevels[it],
                        onYogaDifficultySelection = { difficultyLevel ->
                            workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                            // navigate to yoga screen
                            workoutViewModel.getYogaPosesFromApi()
                            navController.navigate(route = UtilityScreenRoutes.YogaPoses.route)
                        },
                        showDivider = false
                    )
                }
            }

            HeaderText(
                heading = "Track Workouts"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(bottomBarColor)
            ) {

                TodayWorkoutCard(
                    category = "Gym",
                    onCategoryItemSelected = {
                        navController.navigate(UtilityScreenRoutes.GymExercisesPerformed.route)
                    },
                    showDivider = false,
                )

                TodayWorkoutCard(
                    category = "Yoga",
                    onCategoryItemSelected = {
                        navController.navigate(UtilityScreenRoutes.YogaPosesPerformed.route)
                    },
                    showDivider = false
                )
            }

            HeaderText(heading = "Fitness News")

            NewsComposable(
                text = "Stay updated with the latest trends & news in the world of fitness.",
                onClick = {
                    workoutViewModel.updateNewsSearchQuery("Gym news and exercises")
                    navController.navigate(UtilityScreenRoutes.NewsArticlesScreen.route)
                },
                icon = R.drawable.dumbbell_svg
            )

            NewsComposable(
                text = "Stay connected with the latest trends and news in the world of yoga and wellness.",
                onClick = {
                    workoutViewModel.updateNewsSearchQuery("Yoga poses")
                    navController.navigate(UtilityScreenRoutes.NewsArticlesScreen.route)
                },
                icon = R.drawable.yoga_svg,
            )
        }
    }
}

@Composable
fun NewsComposable(
    text: String,
    onClick: () -> Unit,
    icon: Any
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MEDIUM_PADDING)
            .clip(RoundedCornerShape(MEDIUM_PADDING))
            .background(bottomBarColor)
            .clickable {
                onClick()
            }

    ) {
        AsyncImage(
            model = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .size(Dimensions.YOGA_AND_DUMBBELL_SVG_SIZE),
            colorFilter = ColorFilter.tint(primaryTextColor)
        )

        Text(
            text = text,
            fontSize = 16.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(4f)
                .fillMaxWidth(),
        )

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = primaryPurple,
            modifier = Modifier
                .padding(horizontal = MEDIUM_PADDING)
        )
    }
}

@Composable
fun TodayWorkoutCard(
    category: String,
    onCategoryItemSelected: () -> Unit,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MEDIUM_PADDING)
                .clickable {
                    onCategoryItemSelected()
                },
        ) {

            Text(
                text = category,
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
                tint = primaryPurple,
                modifier = Modifier.padding(vertical = MEDIUM_PADDING)
            )
        }

        if (showDivider) CustomDivider(
            dividerColor = ColorsUtil.dividerColor,
            verticalPadding = 0.dp
        )
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
            navigateToScreen(UtilityScreenRoutes.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.MUSCLE.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.MUSCLE_TYPE)
            navigateToScreen(UtilityScreenRoutes.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.DIFFICULTY.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.DIFFICULTY_LEVEL)
            navigateToScreen(UtilityScreenRoutes.SelectExerciseCategory.route)
        }

        GymWorkoutCategories.SEARCH.name.lowercase().capitalize() -> {
            updateGymWorkoutCategory(CategoryType.SEARCH_EXERCISE)
            navigateToScreen("SearchScreen/exercises")
        }
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
        modifier = Modifier
            .clickable {
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
            tint = primaryPurple,
            modifier = Modifier.padding(vertical = MEDIUM_PADDING)
        )
    }

    if (showDivider) {
        CustomDivider()
    }
}
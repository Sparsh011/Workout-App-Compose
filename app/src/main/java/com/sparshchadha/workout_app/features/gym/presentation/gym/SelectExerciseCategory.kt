package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.CategoryType
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

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
                topBarDescription = stringResource(id = R.string.select_difficulty_level_heading),
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByDifficultyFromApi(
                        difficultyLevel = it.replace(
                            ' ',
                            '_'
                        )
                    )
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(
                        ScreenRoutes.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }

        CategoryType.WORKOUT_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getWorkoutTypes(),
                topBarDescription = stringResource(id = R.string.select_program_heading),
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByWorkoutTypeFromApi(
                        workoutType = it.replace(
                            ' ',
                            '_'
                        )
                    )
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(
                        ScreenRoutes.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }

        CategoryType.MUSCLE_TYPE -> {
            ExerciseCategoryItems(
                categoryItems = HelperFunctions.getMuscleTypes(),
                topBarDescription = stringResource(id = R.string.select_body_part_heading),
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = {
                    workoutViewModel.getExercisesByMuscleFromApi(muscleType = it.replace(' ', '_'))
                    navController.navigate("ExercisesScreen/$it")
                },
                onBackButtonPressed = {
                    navController.popBackStack(
                        ScreenRoutes.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }

        CategoryType.SEARCH_EXERCISE -> {
            // Handled in WorkoutScreen
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseCategoryItems(
    categoryItems: List<String> = listOf(),
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
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->
        if (topBarDescription == stringResource(id = R.string.select_body_part_heading)) {
            SelectBodyPartExercises(
                localPaddingValues = localPaddingValues,
                globalPaddingValues = globalPaddingValues,
                onCategoryItemSelected = onCategoryItemSelected
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding()
                    )
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)
                        .clip(RoundedCornerShape(MEDIUM_PADDING))
                        .background(bottomBarColor)
                ) {
                    for (categoryItem in categoryItems.indices) {
                        CategoryItem(
                            categoryItem = categoryItems[categoryItem],
                            onCategoryItemSelected = onCategoryItemSelected,
                            showDivider = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectBodyPartExercises(
    localPaddingValues: PaddingValues,
    globalPaddingValues: PaddingValues,
    onCategoryItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBackgroundColor)
            .padding(
                top = localPaddingValues.calculateTopPadding(),
                bottom = globalPaddingValues.calculateBottomPadding()
            )
            .verticalScroll(rememberScrollState())
    ) {

        BodyPartStickyHeader(
            modifier = Modifier.fillMaxWidth(),
            bodyPart = "Chest, Neck & Shoulders"
        )

        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .clip(RoundedCornerShape(MEDIUM_PADDING))
                .background(bottomBarColor)
        ) {
            for (categoryItem in HelperFunctions.getChestShouldersAndNeck().indices) {
                CategoryItem(
                    categoryItem = HelperFunctions.getChestShouldersAndNeck()[categoryItem],
                    onCategoryItemSelected = onCategoryItemSelected,
                    categoryItemFontSize = 16,
                    showDivider = false
                )
            }
        }

        BodyPartStickyHeader(
            modifier = Modifier.fillMaxWidth(),
            bodyPart = "Back"
        )

        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .clip(RoundedCornerShape(MEDIUM_PADDING))
                .background(bottomBarColor)
        ) {
            for (categoryItem in HelperFunctions.getBack().indices) {
                CategoryItem(
                    categoryItem = HelperFunctions.getBack()[categoryItem],
                    onCategoryItemSelected = onCategoryItemSelected,
                    categoryItemFontSize = 16,
                    showDivider = false
                )
            }
        }

        BodyPartStickyHeader(
            modifier = Modifier.fillMaxWidth(),
            bodyPart = "Arms"
        )


        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .clip(RoundedCornerShape(MEDIUM_PADDING))
                .background(bottomBarColor)
        ) {
            for (categoryItem in HelperFunctions.getArms().indices) {
                CategoryItem(
                    categoryItem = HelperFunctions.getArms()[categoryItem],
                    onCategoryItemSelected = onCategoryItemSelected,
                    categoryItemFontSize = 16,
                    showDivider = false
                )
            }
        }

        BodyPartStickyHeader(
            modifier = Modifier.fillMaxWidth(),
            bodyPart = "Legs"
        )

        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .clip(RoundedCornerShape(MEDIUM_PADDING))
                .background(bottomBarColor)
        ) {
            for (categoryItem in HelperFunctions.getLegs().indices) {
                CategoryItem(
                    categoryItem = HelperFunctions.getLegs()[categoryItem],
                    onCategoryItemSelected = onCategoryItemSelected,
                    categoryItemFontSize = 16,
                    showDivider = false
                )
            }
        }

    }
}

@Composable
fun BodyPartStickyHeader(
    modifier: Modifier,
    bodyPart: String,
) {
    Surface(
        modifier = modifier,
        color = scaffoldBackgroundColor
    ) {
        Text(
            text = bodyPart,
            fontSize = 20.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier.padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryItem(
    categoryItem: String,
    onCategoryItemSelected: (String) -> Unit,
    categoryItemFontSize: Int = 16,
    showDivider: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onCategoryItemSelected(categoryItem)
            }
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
    ) {
        Text(
            text = categoryItem,
            fontSize = categoryItemFontSize.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(0.8f)
                .fillMaxWidth()
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = primaryPurple
        )
    }

    if (showDivider) {
        CustomDivider()
    }
}
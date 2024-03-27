package com.sparshchadha.workout_app.shared_ui.screens.shared

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.gym.ExerciseSubTitlesAndDescription
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.shared_ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.shared_ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

private const val TAG = "SavedItemsScreenTagggg"

@Composable
fun SavedItemsScreen(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodAndWaterViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel,
    category: String,
    yogaViewModel: YogaViewModel
) {

    when (category) {
        "gym" -> {
            LaunchedEffect(key1 = Unit) {
                workoutViewModel.getSavedExercises()
            }

            SavedGymExercises(
                exercises = workoutViewModel.savedExercises.collectAsStateWithLifecycle().value
                    ?: emptyList(),
                navigateToExerciseDetailsScreen = { forExercise ->
                    workoutViewModel.updateSelectedExercise(forExercise.exerciseDetails)
                    navController.navigate(UtilityScreenRoutes.ExerciseDetailScreen.route)
                },
                removeFromSaved = {
                    workoutViewModel.removeExerciseFromDB(it)
                },
                globalPaddingValues = globalPaddingValues,
                navController = navController
            )
        }

        "yoga" -> {
            LaunchedEffect(key1 = Unit) {
                yogaViewModel.getSavedYogaPoses()
            }

            SavedYogaPoses(
                poses = yogaViewModel.savedPoses.collectAsStateWithLifecycle().value
                    ?: emptyList(),
                navigateToYogaPoseDetailsScreen = { forPose ->

                },
                removeYogaPoseFromSaved = {
                    yogaViewModel.removeYogaPoseFromDB(it)
                },
                updateYogaEntityToShowDetails = {
                    yogaViewModel.updateSelectedYogaPose(it.yogaPoseDetails)
                },
                globalPaddingValues = globalPaddingValues,
                navController = navController
            )
        }

        "calorieTracker" -> {
            LaunchedEffect(key1 = Unit) {
                foodAndWaterViewModel.getSavedFoodItems()
            }

            SavedFoodItem(
                foodItems = foodAndWaterViewModel.savedFoodItems.collectAsStateWithLifecycle().value
                    ?: emptyList(),
                navigateToFoodItemDetailsScreen = { forItem ->

                },
                removeFoodItemFromSaved = {
                    foodAndWaterViewModel.removeFoodItem(it)
                },
                updateFoodItemToShowDetails = {
                    foodAndWaterViewModel.updateSelectedFoodItem(it.foodItemDetails)
                },
                globalPaddingValues = globalPaddingValues,
                navController = navController
            )
        }
    }
}

@Composable
fun SavedFoodItem(
    foodItems: List<FoodItemEntity>,
    navigateToFoodItemDetailsScreen: (String) -> Unit,
    removeFoodItemFromSaved: (FoodItemEntity) -> Unit,
    updateFoodItemToShowDetails: (FoodItemEntity) -> Unit,
    globalPaddingValues: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) {
        ScaffoldTopBar(
            topBarDescription = "Saved Food Items",
            onBackButtonPressed = {
                navController.popBackStack()
            }
        )

        if (foodItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(foodItems) {
                    SavedFoodItem(
                        foodItem = it,
                        removeFromSaved = removeFoodItemFromSaved,
                        navigateToScreen = { route ->
                            updateFoodItemToShowDetails(it)
                            navigateToFoodItemDetailsScreen(route)
                        }
                    )
                }
            }
        } else {
            NoSavedItem()
        }
    }
}

@Composable
fun SavedGymExercises(
    exercises: List<GymExercisesEntity>,
    navigateToExerciseDetailsScreen: (GymExercisesEntity) -> Unit,
    removeFromSaved: (GymExercisesEntity) -> Unit,
    globalPaddingValues: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) {
        ScaffoldTopBar(
            topBarDescription = "Saved Exercises",
            onBackButtonPressed = {
                navController.popBackStack()
            }
        )

        if (exercises.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(MEDIUM_PADDING)
                    .clip(RoundedCornerShape(MEDIUM_PADDING))
                    .background(bottomBarColor)
            ) {
                items(exercises) {
                    SavedExercise(
                        exercise = it,
                        removeFromSaved = removeFromSaved,
                        navigateToScreen = navigateToExerciseDetailsScreen
                    )
                }
            }
        } else {
            NoSavedItem()
        }
    }
}

@Composable
fun SavedExercise(
    exercise: GymExercisesEntity,
    removeFromSaved: (GymExercisesEntity) -> Unit,
    navigateToScreen: (GymExercisesEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clickable {
                navigateToScreen(exercise)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(SMALL_PADDING)
                .weight(4f),
        ) {
            ExerciseSubTitlesAndDescription(
                title = exercise.exerciseDetails?.name ?: "Unable To Get Name",
                description = ("Requires " + exercise.exerciseDetails?.equipment),
                showDescription = true,
                descriptionColor = targetAchievedColor
            )
        }

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.clickable {
                removeFromSaved(exercise)
            },
            tint = ColorsUtil.noAchievementColor
        )
    }
}

@Composable
fun SavedYogaPoses(
    poses: List<YogaEntity>,
    navigateToYogaPoseDetailsScreen: (String) -> Unit,
    removeYogaPoseFromSaved: (YogaEntity) -> Unit,
    updateYogaEntityToShowDetails: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
            .clickable {

            }
    ) {
        ScaffoldTopBar(
            topBarDescription = "Saved Yoga Poses",
            onBackButtonPressed = {
                navController.popBackStack()
            }
        )

        if (poses.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(poses) {
                    SavedYogaPose(
                        pose = it,
                        removeFromSaved = removeYogaPoseFromSaved,
                        navigateToScreen = { route ->
                            updateYogaEntityToShowDetails(it)
                            navigateToYogaPoseDetailsScreen(route)
                        }
                    )
                }

            }
        } else {
            NoSavedItem()
        }
    }
}

@Composable
fun SavedFoodItem(
    foodItem: FoodItemEntity,
    removeFromSaved: (FoodItemEntity) -> Unit,
    navigateToScreen: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = foodItem.foodItemDetails?.name ?: "Unable To Get Name",
            fontSize = 20.nonScaledSp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(SMALL_PADDING)
                .weight(4f),
            color = primaryTextColor
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.clickable {
                removeFromSaved(foodItem)
            },
            tint = ColorsUtil.noAchievementColor
        )
    }

    CustomDivider()
}

@Composable
fun SavedYogaPose(
    pose: YogaEntity,
    removeFromSaved: (YogaEntity) -> Unit,
    navigateToScreen: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pose.yogaPoseDetails?.english_name ?: "Unable To Get Name",
            fontSize = 20.nonScaledSp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(SMALL_PADDING)
                .weight(4f),
            color = primaryTextColor
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.clickable {
                removeFromSaved(pose)
            },
            tint = ColorsUtil.noAchievementColor
        )
    }

    CustomDivider()
}
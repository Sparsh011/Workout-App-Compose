package com.sparshchadha.workout_app.ui.screens.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.GymExercisesEntity
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

private const val TAG = "SavedItemsScreenTagggg"

@Composable
fun SavedItemsScreen(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
    workoutViewModel: WorkoutViewModel,
    category: String
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

                },
                removeFromSaved = {
                    workoutViewModel.removeExerciseFromDB(it)
                },
                updateExerciseToShowDetails = {
                    workoutViewModel.updateSelectedExercise(it.exerciseDetails)
                },
                globalPaddingValues = globalPaddingValues,
                navController = navController
            )
        }

        "yoga" -> {
            LaunchedEffect(key1 = Unit) {
                workoutViewModel.getSavedYogaPoses()
            }

            SavedYogaPoses(
                poses = workoutViewModel.savedPoses.collectAsStateWithLifecycle().value
                    ?: emptyList(),
                navigateToYogaPoseDetailsScreen = { forPose ->

                },
                removeYogaPoseFromSaved = {
                    workoutViewModel.removeYogaPoseFromDB(it)

                },
                updateYogaEntityToShowDetails = {
                    workoutViewModel.updateSelectedYogaPose(it.yogaPoseDetails)
                },
                globalPaddingValues = globalPaddingValues,
                navController = navController
            )
        }

        "calorieTracker" -> {
            LaunchedEffect(key1 = Unit) {
                foodItemsViewModel.getSavedFoodItems()
            }

            SavedFoodItem(
                foodItems = foodItemsViewModel.savedFoodItems.collectAsStateWithLifecycle().value
                    ?: emptyList(),
                navigateToFoodItemDetailsScreen = { forItem ->

                },
                removeFoodItemFromSaved = {
                    foodItemsViewModel.removeFoodItem(it)
                },
                updateFoodItemToShowDetails = {
                    foodItemsViewModel.updateSelectedFoodItem(it.foodItemDetails)
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
    navigateToExerciseDetailsScreen: (String) -> Unit,
    removeFromSaved: (GymExercisesEntity) -> Unit,
    updateExerciseToShowDetails: (GymExercisesEntity) -> Unit,
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
                modifier = Modifier.fillMaxSize()
            ) {
                items(exercises) {
                    SavedExercise(
                        exercise = it,
                        removeFromSaved = removeFromSaved,
                        navigateToScreen = { route ->
                            updateExerciseToShowDetails(it)
                            navigateToExerciseDetailsScreen(route)
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
fun SavedExercise(
    exercise: GymExercisesEntity,
    removeFromSaved: (GymExercisesEntity) -> Unit,
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
            text = exercise.exerciseDetails?.name ?: "Unable To Get Name",
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
                removeFromSaved(exercise)
            },
            tint = ColorsUtil.noAchievementColor
        )
    }

    CustomDivider()
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
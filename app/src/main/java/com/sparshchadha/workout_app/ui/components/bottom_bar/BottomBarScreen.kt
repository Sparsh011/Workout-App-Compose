package com.sparshchadha.workout_app.ui.components.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object CalorieTracker: BottomBarScreen(
        route = "CalorieTrackerScreen",
        title = "Track Calories",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object WorkoutScreen: BottomBarScreen(
        route = "WorkoutScreen",
        title = "Workout",
        selectedIcon = Icons.Default.List,
        unselectedIcon = Icons.Outlined.List
    )

    object ProfileScreen: BottomBarScreen(
        route = "ProfileScreen",
        title = "Profile",
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )
}

sealed class UtilityScreen(
    val route: String,
    val title: String
) {
    object SearchScreen: UtilityScreen(
        route = "SearchScreen/{searchFor}",
        title = "Search"
    )

    object YogaPoses: UtilityScreen(
        route = "YogaPosesScreen",
        title = "Yoga Poses"
    )

    object SelectExerciseCategory: UtilityScreen(
        route = "SelectExerciseCategory",
        title = "Select Exercise Category"
    )

    object ExercisesScreen: UtilityScreen(
        route = "ExercisesScreen/{category}",
        title = "Exercises"
    )

    object YogaPosesPerformed: UtilityScreen(
        route = "YogaPosesPerformed",
        title = "Yoga Poses Performed Today"
    )

    object GymExercisesPerformed: UtilityScreen(
        route = "GymExercisesPerformed",
        title = "Gym Exercises Performed Today"
    )

    object FoodItemDetailsScreen: UtilityScreen(
        route = "FoodItemDetails/{foodItemId}",
        title = "Food Item Details"
    )

    object RemindersScreen: UtilityScreen(
        route = "RemindersScreen",
        title = "Reminders Screen"
    )

    object ExerciseDetailScreen: UtilityScreen(
        route = "ExerciseDetailScreen",
        title = "Exercise Details"
    )
}
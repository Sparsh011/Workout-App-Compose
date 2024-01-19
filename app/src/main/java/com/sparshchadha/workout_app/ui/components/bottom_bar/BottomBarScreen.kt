package com.sparshchadha.workout_app.ui.components.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object CalorieTracker: BottomBarScreen(
        route = "CalorieTrackerScreen",
        title = "Track Calories",
        icon = Icons.Default.Home
    )

    object WorkoutScreen: BottomBarScreen(
        route = "WorkoutScreen",
        title = "Workout",
        icon = Icons.Default.List
    )

    object ProfileScreen: BottomBarScreen(
        route = "ProfileScreen",
        title = "Profile",
        icon = Icons.Default.AccountCircle
    )
}

sealed class UtilityScreen(
    val route: String,
    val title: String
) {
    object SearchFood: UtilityScreen(
        route = "SearchFoodScreen",
        title = "Search Food Items"
    )

    object YogaPoses: UtilityScreen(
        route = "YogaPosesScreen",
        title = "Yoga Poses"
    )

    object GymWorkout: UtilityScreen(
        route = "GymWorkoutScreen",
        title = "Gym Workouts"
    )
}
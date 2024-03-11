package com.sparshchadha.workout_app.ui.components.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenRoutes(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    object CalorieTracker : ScreenRoutes(
        route = "CalorieTrackerScreen",
        title = "Track Calories",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object WorkoutScreen : ScreenRoutes(
        route = "WorkoutScreen",
        title = "Workout",
        selectedIcon = Icons.Default.List,
        unselectedIcon = Icons.Outlined.List
    )

    object RemindersScreen : ScreenRoutes(
        route = "RemindersScreen",
        title = "Reminders",
        selectedIcon = Icons.Default.DateRange,
        unselectedIcon = Icons.Outlined.DateRange
    )

    object ProfileScreen : ScreenRoutes(
        route = "ProfileScreen",
        title = "Profile",
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
    )
}

sealed class UtilityScreenRoutes(
    val route: String,
    val title: String,
) {
    object SearchScreen : UtilityScreenRoutes(
        route = "SearchScreen/{searchFor}",
        title = "Search"
    )

    object YogaPoses : UtilityScreenRoutes(
        route = "YogaPosesScreen",
        title = "Yoga Poses"
    )

    object SelectExerciseCategory : UtilityScreenRoutes(
        route = "SelectExerciseCategory",
        title = "Select Exercise Category"
    )

    object ExercisesScreen : UtilityScreenRoutes(
        route = "ExercisesScreen/{category}",
        title = "Exercises"
    )

    object YogaPosesPerformed : UtilityScreenRoutes(
        route = "YogaPosesPerformed",
        title = "Yoga Poses Performed Today"
    )

    object GymExercisesPerformed : UtilityScreenRoutes(
        route = "GymExercisesPerformed",
        title = "Gym Exercises Performed Today"
    )

    object FoodItemDetailsScreen : UtilityScreenRoutes(
        route = "FoodItemDetails/{foodItemId}",
        title = "Food Item Details"
    )

    object ExerciseDetailScreen : UtilityScreenRoutes(
        route = "ExerciseDetailScreen",
        title = "Exercise Details"
    )

    object SavedItemsScreen : UtilityScreenRoutes(
        route = "SavedItemsScreen/{category}",
        title = "Saved Items Screen"
    )

    object PersonalRecordsScreen : UtilityScreenRoutes(
        route = "PersonalRecordsScreen",
        title = "Personal Records Screen"
    )

    object GymActivityScreen : UtilityScreenRoutes(
        route = "GymActivityScreen",
        title = "GymActivityScreen"
    )

    object YogaActivityScreen : UtilityScreenRoutes(
        route = "YogaActivityScreen",
        title = "YogaActivityScreen"
    )

    object NewsArticlesScreen : UtilityScreenRoutes(
        route = "NewsArticlesScreen",
        title = "News Articles Screen"
    )

    object ArticleWebViewScreen : UtilityScreenRoutes(
        route = "ArticleWebViewScreen",
        title = "Article WebView Screen"
    )
}
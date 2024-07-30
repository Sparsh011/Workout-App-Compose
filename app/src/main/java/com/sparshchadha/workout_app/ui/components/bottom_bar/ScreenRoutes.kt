package com.sparshchadha.workout_app.ui.components.bottom_bar

import com.sparshchadha.workout_app.R

sealed class ScreenRoutes(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object CalorieTracker : ScreenRoutes(
        route = "CalorieTrackerScreen",
        title = "Food",
        icon = R.drawable.food_bottom_bar_icon
    )

    object WorkoutScreen : ScreenRoutes(
        route = "WorkoutScreen",
        title = "Workout",
        icon = R.drawable.exercise_bottom_bar_icon,
    )

    object RemindersScreen : ScreenRoutes(
        route = "RemindersScreen",
        title = "Reminders",
        icon = R.drawable.reminder_bottom_bar_icon,
    )

    object ProfileScreen : ScreenRoutes(
        route = "ProfileScreen",
        title = "Profile",
        icon = R.drawable.profile_bottom_bar_icon,
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
        route = "FoodItemDetails",
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

    object CreateDishScreen : UtilityScreenRoutes(
        route = "CreateDishScreen",
        title = "Create Dish Screen"
    )

    object GymGoalsScreen : UtilityScreenRoutes(
        route = "GymGoalsScreen",
        title = "Gym Goals Screen"
    )
}
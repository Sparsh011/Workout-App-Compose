package com.sparshchadha.workout_app.ui.navigation.nav_graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.features.reminders.presentation.viewmodels.RemindersViewModel
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.bottomCalorieTrackerComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.foodItemDetailsComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.bottomWorkoutComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.exerciseDetailsComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymActivityComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymExercisesComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymExercisesPerformedComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.workoutCategoryComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.bottomProfileScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.personalRecordsScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.articleWebViewScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.bottomRemindersComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.newsArticlesComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.savedItemsScreenComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.searchComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaActivityComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaPosesPerformedTodayComposable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    workoutViewModel: WorkoutViewModel,
    remindersViewModel: RemindersViewModel,
    profileViewModel: ProfileViewModel,
    newsViewModel: NewsViewModel,
    yogaViewModel: YogaViewModel,
    toggleBottomBarVisibility: (Boolean) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.CalorieTracker.route
    ) {
        // Workout Tracker in Bottom Bar
        bottomWorkoutComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            newsViewModel = newsViewModel,
            yogaViewModel = yogaViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Calorie Tracker in Bottom Bar
        bottomCalorieTrackerComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            profileViewModel = profileViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Profile Screen
        bottomProfileScreen(
            globalPaddingValues = globalPaddingValues,
            navController = navController,
            profileViewModel = profileViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Reminders Screen
        bottomRemindersComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            remindersViewModel = remindersViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Search Screen
        searchComposable(
            foodAndWaterViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Yoga Screen
        yogaComposable(
            yogaViewModel = yogaViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Selecting Workout Type
        workoutCategoryComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Exercises From Api
        gymExercisesComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Yoga poses performed today
        yogaPosesPerformedTodayComposable(
            yogaViewModel = yogaViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Gym Exercises performed today
        gymExercisesPerformedComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Exercise Details screen
        exerciseDetailsComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // Food Item Details
        foodItemDetailsComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // saved items (food, yoga and gym) screen
        savedItemsScreenComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            yogaViewModel = yogaViewModel,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // personal records screen
        personalRecordsScreen(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // gym activity
        gymActivityComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // yoga activity
        yogaActivityComposable(
            yogaViewModel = yogaViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // news articles screen
        newsArticlesComposable(
            navController = navController,
            newsViewModel = newsViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )

        // news article WebView
        articleWebViewScreen(
            navController = navController,
            newsViewModel = newsViewModel,
            globalPaddingValues = globalPaddingValues,
            toggleBottomBarVisibility = toggleBottomBarVisibility
        )
    }
}


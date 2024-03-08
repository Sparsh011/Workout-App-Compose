package com.sparshchadha.workout_app.ui.navigation.nav_graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.calorieTrackerComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.foodItemDetailsComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.bottomWorkoutComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.exerciseDetailsComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymActivityComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymExercisesComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.gymExercisesPerformedComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.gym.workoutCategoryComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.personalRecordsScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.profileComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.remindersComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.savedItemsScreenComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.shared.searchComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaActivityComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.yoga.yogaPosesPerformedTodayComposable
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
    workoutViewModel: WorkoutViewModel,
    gymExercises: GymExercisesDto?,
    yogaPoses: YogaPosesDto?,
    remindersViewModel: RemindersViewModel,
    profileViewModel: ProfileViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.CalorieTracker.route
    ) {
        // Workout Tracker in Bottom Bar
        bottomWorkoutComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Calorie Tracker in Bottom Bar
        calorieTrackerComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            profileViewModel = profileViewModel
        )

        // Search Screen
        searchComposable(
            foodItemsViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Profile Screen
        profileComposable(
            globalPaddingValues = globalPaddingValues,
            navController = navController,
            profileViewModel = profileViewModel
        )

        // Yoga Screen
        yogaComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            yogaPoses = yogaPoses,
            globalPaddingValues = globalPaddingValues
        )

        // Selecting Workout Type
        workoutCategoryComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Exercises From Api
        gymExercisesComposable(
            navController = navController,
            gymExercises = gymExercises,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )

        // Yoga poses performed today
        yogaPosesPerformedTodayComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Gym Exercises performed today
        gymExercisesPerformedComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Exercise Details screen
        exerciseDetailsComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )

        // Food Item Details
        foodItemDetailsComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel
        )

        // Reminders Screen
        remindersComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            remindersViewModel = remindersViewModel
        )

        // saved items (food, yoga and gym) screen
        savedItemsScreenComposable(
            navController = navController,
            globalPaddingValues = globalPaddingValues,
            foodItemsViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel
        )

        // personal records screen
        personalRecordsScreen(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )

        // gym activity
        gymActivityComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )

        // yoga activity
        yogaActivityComposable(
            navController = navController,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = globalPaddingValues
        )
    }
}


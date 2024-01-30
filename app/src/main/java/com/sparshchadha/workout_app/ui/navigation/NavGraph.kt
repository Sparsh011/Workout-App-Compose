package com.sparshchadha.workout_app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymExercisesDto
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.calorieTrackerComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.profileComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.searchComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.bottomWorkoutComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.gymExercisesComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.workoutCategoryComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.yogaComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.yogaPosesPerformedTodayComposable
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
    workoutViewModel: WorkoutViewModel,
    gymExercises: GymExercisesDto?,
    yogaPoses: YogaPosesDto?
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.WorkoutScreen.route) {
        // Workout Tracker in Bottom Bar
        bottomWorkoutComposable(
            workoutViewModel = workoutViewModel,
            navController = navController
        )

        // Calorie Tracker in Bottom Bar
       calorieTrackerComposable(
           navController = navController,
           globalPaddingValues = globalPaddingValues,
           foodItemsViewModel = foodItemsViewModel
       )

        // Search Screen
        searchComposable(
            foodItemsViewModel = foodItemsViewModel,
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = globalPaddingValues
        )

        // Profile Screen
        profileComposable()

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
    }
}


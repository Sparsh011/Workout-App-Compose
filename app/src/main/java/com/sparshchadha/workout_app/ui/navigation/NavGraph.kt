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
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    searchFoodViewModel: SearchFoodViewModel,
    workoutViewModel: WorkoutViewModel,
    gymExercises: GymExercisesDto?,
    yogaPoses: YogaPosesDto?
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.CalorieTracker.route) {
        // Workout Tracker in Bottom Bar
        bottomWorkoutComposable(
            workoutViewModel = workoutViewModel,
            navController = navController
        )

        // Calorie Tracker in Bottom Bar
       calorieTrackerComposable(
           navController = navController,
           globalPaddingValues = paddingValues
       )

        // Search Screen
        searchComposable(
            searchFoodViewModel = searchFoodViewModel,
            workoutViewModel = workoutViewModel,
            navController = navController,
            globalPaddingValues = paddingValues
        )

        // Profile Screen
        profileComposable()

        // Yoga Screen
        yogaComposable(
            workoutViewModel = workoutViewModel,
            navController = navController,
            yogaPoses = yogaPoses,
            globalPaddingValues = paddingValues
        )

        // Selecting Workout Type
       workoutCategoryComposable(
           workoutViewModel = workoutViewModel,
           navController = navController,
           globalPaddingValues = paddingValues
       )

        // Exercises From Api
        gymExercisesComposable(
            navController = navController,
            gymExercises = gymExercises,
            workoutViewModel = workoutViewModel,
            globalPaddingValues = paddingValues
        )
    }
}


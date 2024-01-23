package com.sparshchadha.workout_app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sparshchadha.workout_app.data.remote.dto.gym_workout.GymWorkoutsDto
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.bottomWorkoutComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.calorie_tracker.calorieTrackerComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.workout.exercisesComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.profile.profileComposable
import com.sparshchadha.workout_app.ui.navigation.destinations.searchComposable
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
    exercises: GymWorkoutsDto?,
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.WorkoutScreen.route) {
        // Workout Tracker in Bottom Bar
        bottomWorkoutComposable(
            workoutViewModel = workoutViewModel,
            navController = navController
        )

        // Calorie Tracker in Bottom Bar
       calorieTrackerComposable(
           navController = navController
       )

        // Search Screen
        searchComposable(
            searchFoodViewModel = searchFoodViewModel,
            workoutViewModel = workoutViewModel,
            navController = navController,
            paddingValues = paddingValues
        )

        // Profile Screen
        profileComposable()

        // Yoga Screen
        yogaComposable(
            workoutViewModel = workoutViewModel,
            navController = navController
        )

        // Selecting Workout Type
       workoutCategoryComposable(
           workoutViewModel = workoutViewModel,
           navController = navController
       )

        // Exercises From Api
        exercisesComposable(
            navController = navController,
            exercises = exercises,
            workoutViewModel = workoutViewModel
        )
    }
}


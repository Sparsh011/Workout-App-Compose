package com.sparshchadha.workout_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.destinations.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivityTaggg"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val foodItemsViewModel : FoodItemsViewModel by viewModels()
    private val workoutViewModel : WorkoutViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WorkoutAppTheme {
                val navHostController = rememberNavController()
                val gymExercises = workoutViewModel.gymExercisesFromApi.value
                val yogaPoses = workoutViewModel.yogaPosesFromApi.value

                Scaffold (
                    bottomBar = {
                        BottomBar(navHostController = navHostController)
                    }
                ){
                    NavGraph(
                        navController = navHostController,
                        globalPaddingValues = it,
                        foodItemsViewModel = foodItemsViewModel,
                        workoutViewModel = workoutViewModel,
                        gymExercises = gymExercises,
                        yogaPoses = yogaPoses
                    )
                }
            }
        }
    }
}

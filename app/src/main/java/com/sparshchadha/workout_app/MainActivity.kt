package com.sparshchadha.workout_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.destinations.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivityTaggg"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val foodItemsViewModel : FoodItemsViewModel by viewModels()
    private val workoutViewModel : WorkoutViewModel by viewModels()
    private val remindersViewModel : RemindersViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WorkoutAppTheme {
                val navHostController = rememberNavController()
                val gymExercises = workoutViewModel.gymExercisesFromApi.value
                val yogaPoses = workoutViewModel.yogaPosesFromApi.value

                val permissionLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted_ ->
                        // this is called when the user selects allow or deny
                        Toast.makeText(
                            this@MainActivity,
                            "permissionGranted_ $permissionGranted_",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "onCreate: $permissionGranted_")
                    }



                Scaffold (
                    bottomBar = {
                        BottomBar(navHostController = navHostController)
                    },
                    containerColor = Color.White
                ){
                    NavGraph(
                        navController = navHostController,
                        globalPaddingValues = it,
                        foodItemsViewModel = foodItemsViewModel,
                        workoutViewModel = workoutViewModel,
                        gymExercises = gymExercises,
                        yogaPoses = yogaPoses,
                        remindersViewModel = remindersViewModel
                    )
                }
            }
        }
    }
}

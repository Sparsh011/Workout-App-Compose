package com.sparshchadha.workout_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val searchFoodViewModel : SearchFoodViewModel by viewModels()
    private val workoutViewModel : WorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WorkoutAppTheme {
                val navHostController = rememberNavController()
                Scaffold (
                    bottomBar = {
                        BottomBar(navHostController = navHostController)
                    }
                ){
                    NavGraph(
                        navController = navHostController,
                        paddingValues = it,
                        searchFoodViewModel = searchFoodViewModel,
                        workoutViewModel = workoutViewModel
                    )
                }
            }
        }
    }
}

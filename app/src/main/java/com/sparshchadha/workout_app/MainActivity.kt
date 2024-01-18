package com.sparshchadha.workout_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.BottomNavGraph
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.SearchDishScreen
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.viewmodel.SearchFoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel : SearchFoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WorkoutAppTheme {
                val navHostController = rememberNavController()
                viewModel.updateSearchQuery("Pizza")
//                viewModel.getFoodItems()
                val showToast = viewModel.showToast.value
                val dishes = viewModel.foodItems.value

                if (showToast) {
                    Toast.makeText(LocalContext.current, "Response successful", Toast.LENGTH_SHORT).show()
                }
//                Scaffold (
//                    bottomBar = {
//                        BottomBar(navHostController = navHostController)
//                    }
//                ){
//                    BottomNavGraph(navController = navHostController, paddingValues = it)
//                }

                SearchDishScreen(vm = viewModel, dishes = dishes)

            }
        }
    }
}

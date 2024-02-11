package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel

@Composable
fun FoodItemDetails(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodItemsViewModel,
    foodItemId: Int?,
) {
    if (foodItemId != null) {
        LaunchedEffect(key1 = true) {
            foodItemsViewModel.getFoodItemById(id = foodItemId)
        }
        val foodItem = foodItemsViewModel.foodItemEntity.value

        Scaffold(
            topBar = {
                ScaffoldTopBar(
                    topBarDescription = foodItem?.foodItemDetails?.name?.capitalize() ?: "Sorry, Unable To Get Name!",
                    onBackButtonPressed = {
                        navController.popBackStack(BottomBarScreen.CalorieTracker.route, false)
                    }
                )
            }
        ) { localPaddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        start = MEDIUM_PADDING,
                        end = MEDIUM_PADDING,
                        bottom = globalPaddingValues.calculateBottomPadding()
                    )
            ) {

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Servings",
                    quantityOfMacroNutrient = foodItem?.servings.toString()
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Calories Per Serving ",
                    quantityOfMacroNutrient = foodItem?.foodItemDetails?.calories.toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Total Calories ",
                    quantityOfMacroNutrient = (foodItem?.servings?.times(
                        (foodItem.foodItemDetails?.calories?.toInt()
                            ?: 0)
                    )).toString() + " KCAL"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Carbohydrates",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.carbohydrates_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Protein",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.protein_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Total Fat",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.fat_total_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Saturated Fat",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.fat_saturated_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Sugar",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.sugar_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Cholesterol",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.cholesterol_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Sodium",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.sodium_mg} mg"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Fiber",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.fiber_g} g"
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Potassium",
                quantityOfMacroNutrient = "${foodItem?.foodItemDetails?.potassium_mg} mg"
                )
            }
        }
    } else {
        // TODO
    }
}

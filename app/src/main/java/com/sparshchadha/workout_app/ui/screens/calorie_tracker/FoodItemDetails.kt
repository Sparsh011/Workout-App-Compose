package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
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
                    topBarDescription = foodItem?.foodItemDetails?.name?.capitalize()
                        ?: "Unable To Get Name!",
                    onBackButtonPressed = {
                        navController.popBackStack(BottomBarScreen.CalorieTracker.route, false)
                    }
                )
            }
        ) { localPaddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(scaffoldBackgroundColor)
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        start = MEDIUM_PADDING,
                        end = MEDIUM_PADDING,
                        bottom = globalPaddingValues.calculateBottomPadding()
                    )
            ) {

                Text(
                    text = "Nutritional information of ${foodItem?.foodItemDetails?.name?.capitalize()} per serving(s) is listed below",
                    textAlign = TextAlign.Start,
                    color = primaryTextColor,
                    fontSize = 20.nonScaledSp,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = MEDIUM_PADDING, top = MEDIUM_PADDING, bottom = MEDIUM_PADDING)
                )

                FoodItemText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MEDIUM_PADDING),
                    macroNutrient = "Servings",
                    quantityOfMacroNutrient = "1"
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

package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Constants
import com.sparshchadha.workout_app.util.Constants.CARBOHYDRATES_TOTAL_G
import com.sparshchadha.workout_app.util.Constants.COLOR_TO_NUTRIENT_MAP
import com.sparshchadha.workout_app.util.Constants.PROTEIN_G
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun FoodItemDetails(
    navController: NavHostController,
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    foodItemId: Int?,
) {
    if (foodItemId != null) {
        LaunchedEffect(key1 = true) {
            foodItemsViewModel.getFoodItemById(id = foodItemId)
        }
        Log.i("MacroNutrientsConsumed", "FoodItemDetails: called for food entity")
        val foodItem = foodItemsViewModel.foodItemEntity.value

        Scaffold(
            topBar = {
                ScaffoldTopBar(
                    topBarDescription = foodItem?.foodItemDetails?.name?.capitalize()
                        ?: "Unable To Get Name!",
                    onBackButtonPressed = {
                        navController.popBackStack(ScreenRoutes.CalorieTracker.route, false)
                    }
                )
            }
        ) { localPaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(scaffoldBackgroundColor)
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        start = MEDIUM_PADDING,
                        end = MEDIUM_PADDING,
                        bottom = globalPaddingValues.calculateBottomPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Nutritional information of ${foodItem?.foodItemDetails?.name?.capitalize()} per serving(s) is listed below",
                    textAlign = TextAlign.Start,
                    color = primaryTextColor,
                    fontSize = 20.nonScaledSp,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        end = MEDIUM_PADDING,
                        top = MEDIUM_PADDING,
                        bottom = MEDIUM_PADDING
                    )
                )

                NutritionalValues(foodItem = foodItem)

                Spacer(modifier = Modifier.height(LARGE_PADDING))

                MacrosPieChart(foodItem = foodItem)

                Spacer(modifier = Modifier.height(LARGE_PADDING))
            }
        }
    } else {
        // TODO
    }
}

@Composable
fun MacrosPieChart(foodItem: FoodItemEntity?) {
    val pieCharEntries = mutableListOf<PieChartEntry>()

    val totalMacros by remember {
        mutableDoubleStateOf(
            (foodItem?.foodItemDetails?.fat_total_g ?: 0.0) +
                    (foodItem?.foodItemDetails?.protein_g ?: 0.0) +
                    (foodItem?.foodItemDetails?.carbohydrates_total_g ?: 0.0)
        )
    }

    val protein = (foodItem?.foodItemDetails?.protein_g ?: 0.0 )
    val carbohydrates = (foodItem?.foodItemDetails?.carbohydrates_total_g ?: 0.0 )
    val fats = (foodItem?.foodItemDetails?.protein_g ?: 0.0 )

    if (protein > 0.0) {
        pieCharEntries.add(
            PieChartEntry(
                color = COLOR_TO_NUTRIENT_MAP[PROTEIN_G]!!,
                percentage = ( protein / totalMacros).toFloat()
            )
        )
    }

    if (fats > 0.0) {
        pieCharEntries.add(
            PieChartEntry(
                color = COLOR_TO_NUTRIENT_MAP[Constants.FAT_TOTAL_G]!!,
                percentage = ((foodItem?.foodItemDetails?.fat_total_g ?: 0.0 ) / totalMacros).toFloat()
            )
        )
    }


    if (carbohydrates > 0.0) {
        pieCharEntries.add(
            PieChartEntry(
                color = COLOR_TO_NUTRIENT_MAP[CARBOHYDRATES_TOTAL_G]!!,
                percentage = ((foodItem?.foodItemDetails?.carbohydrates_total_g ?: 0.0 ) / totalMacros).toFloat()
            )
        )
    }

    Log.d("MacroNutrientsConsumed", "MacrosPieChart: calling pie chart")
    PieChart(entries = pieCharEntries, size = Dimensions.PIE_CHART_SIZE)
}

@Composable
fun NutritionalValues(foodItem: FoodItemEntity?) {
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

@Composable
fun FoodItemText(
    modifier: Modifier = Modifier,
    title: String = "Pasta",
    macroNutrient: String = "",
    quantityOfMacroNutrient: String = "",
) {
    if (macroNutrient.isBlank()) {
        Text(
            text = title,
            modifier = modifier,
            textAlign = TextAlign.Center,
            fontSize = 24.nonScaledSp,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = macroNutrient,
                textAlign = TextAlign.Start,
                fontSize = 18.nonScaledSp,
                modifier = Modifier
                    .weight(2f)
                    .padding(MEDIUM_PADDING),
                fontWeight = FontWeight.Bold,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = quantityOfMacroNutrient,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING),
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Normal,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis
            )
        }

        CustomDivider()
    }
}

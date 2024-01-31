package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.ui.components.PickNumberOfSetsOrQuantity
import com.sparshchadha.workout_app.ui.components.rememberPickerState
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.TITLE_SIZE
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun FoodCard(
    expandCard: () -> Unit,
    collapseCard: () -> Unit,
    shouldExpandCard: Boolean,
    foodItem: FoodItem,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
) {
    FoodCardComposable(
        foodItemName = foodItem.name.capitalize(),
        calories = foodItem.calories.toString(),
        sugar = foodItem.sugar_g.toString(),
        fiber = foodItem.fiber_g.toString(),
        sodium = foodItem.sodium_mg.toString(),
        cholesterol = foodItem.cholesterol_mg.toString(),
        protein = foodItem.protein_g.toString(),
        carbohydrates = foodItem.carbohydrates_total_g.toString(),
        servingSize = foodItem.serving_size_g.toString(),
        totalFat = foodItem.fat_total_g.toString(),
        saturatedFat = foodItem.fat_saturated_g.toString(),
        collapseCard = {
            collapseCard()
        },
        isExpanded = shouldExpandCard,
        expandCard = {
            expandCard()
        },
        saveFoodItemWithQuantity = saveFoodItemWithQuantity,
        foodItem = foodItem
    )
}

@Composable
private fun FoodCardComposable(
    foodItemName: String,
    calories: String,
    sugar: String,
    fiber: String,
    sodium: String,
    cholesterol: String,
    protein: String,
    carbohydrates: String,
    servingSize: String,
    totalFat: String,
    saturatedFat: String,
    collapseCard: () -> Unit,
    isExpanded: Boolean,
    expandCard: () -> Unit,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
    foodItem: FoodItem,
) {
    var showBottomSheetToSelectFoodItemQuantity by remember {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryGreenCardBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                toggleCardExpansion(
                    isExpanded = isExpanded,
                    collapseCard = collapseCard,
                    expandCard = expandCard
                )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = foodItemName,
                modifier = Modifier.weight(0.7f),
                fontWeight = FontWeight.Bold,
                fontSize = TITLE_SIZE,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = ColorsUtil.primaryDarkGray,
                modifier = Modifier.clickable {
                    showBottomSheetToSelectFoodItemQuantity = true
                }
            )
        }

        NutritionalValueText(nutrient = "Calories", quantityOfNutrient = calories, unit = "kcal")
        NutritionalValueText(nutrient = "Serving Size", quantityOfNutrient = servingSize)

        if (isExpanded) {
            NutritionalValueText(nutrient = "Protein", quantityOfNutrient = protein)
            NutritionalValueText(nutrient = "Carbohydrates", quantityOfNutrient = carbohydrates)
            NutritionalValueText(nutrient = "Sugar", quantityOfNutrient = sugar)
            NutritionalValueText(nutrient = "Total Fats", quantityOfNutrient = totalFat)
            NutritionalValueText(nutrient = "Saturated Fat", quantityOfNutrient = saturatedFat)
            NutritionalValueText(nutrient = "Fiber", quantityOfNutrient = fiber)
            NutritionalValueText(nutrient = "Sodium", quantityOfNutrient = sodium, unit = "mg")
            NutritionalValueText(nutrient = "Cholesterol", quantityOfNutrient = cholesterol, unit = "mg")
        }

        if (showBottomSheetToSelectFoodItemQuantity) {
            ShowQuantityOrSetsPicker(
                hideQuantityOrSetsPickerBottomSheet = {
                    showBottomSheetToSelectFoodItemQuantity = false
                },
                saveQuantityOrSets = { quantity ->
                    saveFoodItemWithQuantity(
                        FoodItemEntity(
                            date = HelperFunctions.getCurrentDateAndMonth().first.toString(),
                            month = HelperFunctions.getCurrentDateAndMonth().second,
                            quantity = quantity,
                            foodItemDetails = foodItem
                        )
                    )
                },
                title = "Food Quantity"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowQuantityOrSetsPicker(
    hideQuantityOrSetsPickerBottomSheet: () -> Unit,
    saveQuantityOrSets: (Int) -> Unit,
    title: String,
) {
    val pickerState = rememberPickerState()
    ModalBottomSheet(
        onDismissRequest = {
            hideQuantityOrSetsPickerBottomSheet()
        },
        sheetState = rememberModalBottomSheetState(),
        windowInsets = WindowInsets(0, 0, 0, 10),
        containerColor = Color.White
    ) {
        Text(
            buildAnnotatedString {
                append("Select ")
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.primaryDarkTextColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(title)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Start,
            color = ColorsUtil.primaryDarkTextColor,
        )
        Spacer(modifier = Modifier.height(10.dp))

        PickNumberOfSetsOrQuantity(
            items = HelperFunctions.getNumberOfSetsOrQuantity(),
            state = pickerState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textModifier = Modifier.padding(15.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                saveQuantityOrSets(pickerState.selectedItem.toInt())
                hideQuantityOrSetsPickerBottomSheet()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorsUtil.primaryDarkTextColor
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Add ${pickerState.selectedItem} ", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun toggleCardExpansion(isExpanded: Boolean, collapseCard: () -> Unit, expandCard: () -> Unit) {
    if (isExpanded) {
        collapseCard()
    } else {
        expandCard()
    }
}

@Composable
fun NutritionalValueText(nutrient: String, quantityOfNutrient: String, unit: String = "grams") {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = ColorsUtil.primaryDarkTextColor, fontWeight = FontWeight.Bold)) {
                append("$nutrient : ")
            }
            append("$quantityOfNutrient $unit")
        },
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp),
        color = ColorsUtil.primaryDarkTextColor
    )
}

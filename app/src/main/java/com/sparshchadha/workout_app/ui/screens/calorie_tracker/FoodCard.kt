package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.sparshchadha.workout_app.data.local.room_db.entities.FoodItemEntity
import com.sparshchadha.workout_app.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.ui.components.shared.PickNumberOfSetsOrQuantity
import com.sparshchadha.workout_app.ui.components.shared.rememberPickerState
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.cardBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Dimensions.TITLE_SIZE
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.HelperFunctions.noRippleClickable

private const val TAG = "FoodCardTaggg"

@Composable
fun FoodCard(
    expandCard: () -> Unit,
    collapseCard: () -> Unit,
    shouldExpandCard: Boolean,
    foodItem: FoodItem,
    saveFoodItemWithQuantity: (FoodItemEntity) -> Unit,
) {
    FoodItemDetailsCard(
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
private fun FoodItemDetailsCard(
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
            containerColor = cardBackgroundColor
        ),
        modifier = Modifier
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
            .fillMaxWidth()
            .noRippleClickable {
                toggleCardExpansion(
                    isExpanded = isExpanded,
                    collapseCard = collapseCard,
                    expandCard = expandCard
                )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = LARGE_PADDING, vertical = SMALL_PADDING)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = foodItemName,
                modifier = Modifier.weight(0.7f),
                fontWeight = FontWeight.Bold,
                fontSize = TITLE_SIZE,
                color = primaryTextColor
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = primaryTextColor,
                modifier = Modifier.clickable {
                    showBottomSheetToSelectFoodItemQuantity = true
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING, vertical = SMALL_PADDING)
        ) {
            Text(
                text = "$calories kcal",
                color = targetAchievedColor,
                fontSize = 14.nonScaledSp
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING))
            Text(
                text = "/ $servingSize g serving",
                color = primaryTextColor,
                fontSize = 14.nonScaledSp
            )
        }

        if (isExpanded) {
            NutritionalValueText(nutrient = "Protein", quantityOfNutrient = protein)
            NutritionalValueText(nutrient = "Carbohydrates", quantityOfNutrient = carbohydrates)
            NutritionalValueText(nutrient = "Sugar", quantityOfNutrient = sugar)
            NutritionalValueText(nutrient = "Total Fats", quantityOfNutrient = totalFat)
            NutritionalValueText(nutrient = "Saturated Fat", quantityOfNutrient = saturatedFat)
            NutritionalValueText(nutrient = "Fiber", quantityOfNutrient = fiber)
            NutritionalValueText(nutrient = "Sodium", quantityOfNutrient = sodium, unit = "mg")
            NutritionalValueText(
                nutrient = "Cholesterol",
                quantityOfNutrient = cholesterol,
                unit = "mg"
            )
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
                            servings = quantity,
                            foodItemDetails = foodItem
                        )
                    )
                },
                title = "Servings"
            )
        }

        Spacer(modifier = Modifier.height(SMALL_PADDING))
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
        containerColor = bottomBarColor
    ) {
        Text(
            buildAnnotatedString {
                append("Select ")
                withStyle(
                    style = SpanStyle(
                        color = primaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(title)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING),
            textAlign = TextAlign.Start,
            color = primaryTextColor,
        )

        Spacer(modifier = Modifier.height(SMALL_PADDING))

        PickNumberOfSetsOrQuantity(
            items = HelperFunctions.getNumberOfSetsOrQuantity(),
            state = pickerState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textModifier = Modifier.padding(MEDIUM_PADDING)
        )

        Spacer(modifier = Modifier.height(LARGE_PADDING))

        Button(
            onClick = {
                saveQuantityOrSets(pickerState.selectedItem.toInt())
                hideQuantityOrSetsPickerBottomSheet()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryPurple
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Text(text = "Add ${pickerState.selectedItem} $title", color = Color.White)
        }

        Spacer(modifier = Modifier.height(LARGE_PADDING))
    }
}

private fun toggleCardExpansion(
    isExpanded: Boolean,
    collapseCard: () -> Unit,
    expandCard: () -> Unit
) {
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
            withStyle(style = SpanStyle(color = primaryTextColor, fontWeight = FontWeight.Bold)) {
                append("$nutrient : ")
            }
            append("$quantityOfNutrient $unit")
        },
        modifier = Modifier
            .padding(horizontal = LARGE_PADDING, vertical = SMALL_PADDING),
        color = primaryTextColor
    )
}

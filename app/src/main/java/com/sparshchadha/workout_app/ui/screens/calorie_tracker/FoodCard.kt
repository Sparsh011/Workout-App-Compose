package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.TITLE_SIZE

@Composable
fun FoodCard(
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
    expandCard: () -> Unit,
    collapseCard: () -> Unit,
    expandCardState: Boolean,
) {

    FoodCardComposable(
        foodItemName,
        calories,
        sugar,
        fiber,
        sodium,
        cholesterol,
        protein,
        carbohydrates,
        servingSize,
        totalFat,
        saturatedFat,
        collapseCard = {
            collapseCard()
        },
        isExpanded = expandCardState,
        expandCard = {
            expandCard()
        }
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
    expandCard: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryFoodCardBackground
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
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = ColorsUtil.primaryDarkGray)
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
            withStyle(style = SpanStyle(color = ColorsUtil.primaryBlack, fontWeight = FontWeight.Bold)) {
                append("$nutrient : ")
            }
            append("$quantityOfNutrient $unit")
        },
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp),
        color = ColorsUtil.primaryBlack
    )
}

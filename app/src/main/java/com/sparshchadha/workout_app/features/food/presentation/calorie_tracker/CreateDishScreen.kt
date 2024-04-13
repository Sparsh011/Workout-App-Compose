package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions

@Composable
fun CreateDishScreen(
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    navController: NavController
) {
    var dishName by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fatsTotal by remember { mutableStateOf("") }
    var fatsSaturated by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var cholestrol by remember { mutableStateOf("") }
    var fiber by remember { mutableStateOf("") }
    var potassium by remember { mutableStateOf("") }
    var sodium by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Create Dish",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .padding(top = innerPaddingValues.calculateTopPadding(), start = Dimensions.MEDIUM_PADDING, end = Dimensions.MEDIUM_PADDING)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            // Dish name
            OutlinedTextField(
                value = dishName,
                onValueChange = { dishName = it },
                label = { Text("Dish Name (Mandatory)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Calories (kcal)",
                quantity = calories,
                updateQuantity = { calories = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            // Optionals - Carbs, Protein, Fats
            QuantityTextField(
                label = "Carbs (g)",
                quantity = carbs,
                updateQuantity = { carbs = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Protein (g)",
                quantity = protein,
                updateQuantity = { protein = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Fats (g)",
                quantity = fatsTotal,
                updateQuantity = { fatsTotal = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Sugar (g)",
                quantity = sugar,
                updateQuantity = { sugar = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Potassium (mg)",
                quantity = potassium,
                updateQuantity = { potassium = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Fiber (g)",
                quantity = fiber,
                updateQuantity = { fiber = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Sodium (mg)",
                quantity = sodium,
                updateQuantity = { sodium = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Cholesterol (mg)",
                quantity = cholestrol,
                updateQuantity = { cholestrol = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Saturated Fats (mg)",
                quantity = fatsSaturated,
                updateQuantity = { fatsSaturated = it }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            // Pie chart to show dish macros

            Button(
                onClick = {
                    foodItemsViewModel.saveFoodItem(
                        foodItemEntity = FoodItemEntity(
                            date = "-1",
                            month = "-1",
                            servings = -1,
                            hour = -1,
                            minutes = -1,
                            seconds = -1,
                            foodItemDetails = FoodItem(
                                calories = calories.toDouble(),
                                carbohydrates_total_g = carbs.toDouble(),
                                cholesterol_mg = cholestrol.toInt(),
                                fat_saturated_g = fatsSaturated.toDouble(),
                                fat_total_g = fatsTotal.toDouble(),
                                fiber_g = fiber.toDouble(),
                                name = dishName,
                                potassium_mg = potassium.toInt(),
                                protein_g = protein.toDouble(),
                                serving_size_g = -1.0,
                                sodium_mg = sodium.toInt(),
                                sugar_g = sugar.toDouble()
                            )
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorsUtil.primaryPurple
                )
            ) {
                Text("Create Dish", color = Color.White)
            }
        }
    }
}

@Composable
private fun QuantityTextField(
    label: String,
    quantity: String,
    updateQuantity: (String) -> Unit,
) {
    OutlinedTextField(
        value = quantity,
        onValueChange = { updateQuantity(it) },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}


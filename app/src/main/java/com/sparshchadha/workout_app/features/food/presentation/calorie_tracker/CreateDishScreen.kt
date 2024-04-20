package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.FoodItem
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.ImageSelectors
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions

@Composable
fun CreateDishScreen(
    globalPaddingValues: PaddingValues,
    foodItemsViewModel: FoodAndWaterViewModel,
    sharedViewModel: SharedViewModel,
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
    val context = LocalContext.current
    val imageSize = LocalConfiguration.current.screenHeightDp / 2
    val selectedImageUri = sharedViewModel.selectedImageUri.collectAsStateWithLifecycle().value
    val imagePath = sharedViewModel.selectedImagePath.collectAsStateWithLifecycle().value ?: ""

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Create Dish",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        },
        containerColor = ColorsUtil.scaffoldBackgroundColor
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPaddingValues.calculateTopPadding(),
                    start = Dimensions.MEDIUM_PADDING,
                    end = Dimensions.MEDIUM_PADDING
                )
                .verticalScroll(rememberScrollState())
        ) {

            selectedImageUri?.let {
                DishImage(
                    hasUri = true,
                    uri = it,
                    imageSize = imageSize.dp,
                    onClick = {
                        sharedViewModel.openGallery(imageSelector = ImageSelectors.DISH)
                    }
                )
            } ?: run {
                DishImage(
                    hasUri = false,
                    imageSize = imageSize.dp,
                    onClick = {
                        sharedViewModel.openGallery(imageSelector = ImageSelectors.DISH)
                    }
                )
            }

            // Dish name
            OutlinedTextField(
                value = dishName,
                onValueChange = { dishName = it },
                label = { Text("Dish Name") },
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
//                    if (isEmpty(
//                            imagePath,
//                            dishName,
//                            calories,
//                            carbs,
//                            cholestrol,
//                            fatsTotal,
//                            fatsSaturated,
//                            fiber,
//                            potassium,
//                            protein,
//                            sodium,
//                            sugar
//                        )
//                    ) {
//                        if (imagePath.isBlank()) {
//                            Toast.makeText(
//                                context,
//                                "Unable To Get Image Path, Please Try Selecting Image Again!",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        } else {
//                            Toast.makeText(context, "No Field Should Be Empty", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//
//                    } else {
                        foodItemsViewModel.saveFoodItem(
                            foodItemEntity = FoodItemEntity(
                                imageStr = imagePath,
                                date = "-1",
                                month = "-1",
                                servings = -99,
                                hour = -1,
                                minutes = -1,
                                seconds = -1,
                                isConsumed = false,
                                isCreated = true,
                                foodItemDetails = FoodItem(
                                    name = dishName,
                                    calories = 1.0,
                                    carbohydrates_total_g = 1.0,
                                    cholesterol_mg = 1,
                                    fat_total_g = 1.0,
                                    fat_saturated_g = 1.0,
                                    protein_g = 1.0,
                                    sodium_mg = 1,
                                    potassium_mg = 1,
                                    serving_size_g = 1.0,

                                    sugar_g = 1.0,
                                    fiber_g = 1.0

//                                    calories = calories.toDouble(),
//                                    carbohydrates_total_g = carbs.toDouble(),
//                                    cholesterol_mg = cholestrol.toInt(),
//                                    fat_saturated_g = fatsSaturated.toDouble(),
//                                    fat_total_g = fatsTotal.toDouble(),
//                                    fiber_g = fiber.toDouble(),
//                                    potassium_mg = potassium.toInt(),
//                                    protein_g = protein.toDouble(),
//                                    serving_size_g = -1.0,
//                                    sodium_mg = sodium.toInt(),
//                                    sugar_g = sugar.toDouble()
                                )
                            )
                        )
//                    }
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

private fun isEmpty(vararg items: String): Boolean {
    for (item in items) {
        if (item.isBlank()) return true
    }

    return false
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
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun DishImage(
    uri: Uri? = null,
    hasUri: Boolean,
    imageSize: Dp,
    onClick: () -> Unit
) {
    if (hasUri && uri != null) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clickable {
                    onClick()
                }
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.baseline_image_24),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clickable {
                    onClick()
                }
        )
    }
}


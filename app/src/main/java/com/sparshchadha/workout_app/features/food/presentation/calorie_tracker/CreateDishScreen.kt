package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.ImageSelectors
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

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
    var cholesterol by remember { mutableStateOf("") }
    var servingSize by remember {
        mutableStateOf("")
    }
    var fiber by remember { mutableStateOf("") }
    var potassium by remember { mutableStateOf("") }
    var sodium by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    val context = LocalContext.current
    val imageSize = LocalConfiguration.current.screenHeightDp / 3
    val selectedImageUri = sharedViewModel.selectedImageUri.collectAsStateWithLifecycle().value
    val imagePath = sharedViewModel.selectedImagePath.collectAsStateWithLifecycle().value ?: ""

    var expandMacroNutrients by remember {
        mutableStateOf(false)
    }

    var expandRemainingNutrients by remember {
        mutableStateOf(false)
    }

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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = ColorsUtil.noAchievementColor,
                    focusedBorderColor = ColorsUtil.noAchievementColor,
                    unfocusedBorderColor = ColorsUtil.noAchievementColor,
                    errorBorderColor = ColorsUtil.noAchievementColor,
                    focusedLabelColor = ColorsUtil.primaryTextColor,
                    disabledLabelColor = ColorsUtil.primaryTextColor,
                    unfocusedLabelColor = ColorsUtil.primaryTextColor,
                    errorLabelColor = ColorsUtil.primaryTextColor
                )
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Calories (kcal)",
                quantity = calories,
                updateQuantity = { calories = it },
                isMandatory = true
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            QuantityTextField(
                label = "Serving size (g)",
                quantity = servingSize,
                updateQuantity = { servingSize = it },
                isMandatory = true
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            Header(
                text = "Add Protein, Carbs and Fats",
                icon = if (expandMacroNutrients) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                onClick = {
                    expandMacroNutrients = !expandMacroNutrients
                }
            )

            AnimatedVisibility(visible = expandMacroNutrients) {
                Column {
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
                }
            }

            Header(
                text = "Add Remaining Nutrients",
                icon = if (expandRemainingNutrients) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                onClick = {
                    expandRemainingNutrients = !expandRemainingNutrients
                }
            )

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            AnimatedVisibility(visible = expandRemainingNutrients) {
                Column {
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
                        quantity = cholesterol,
                        updateQuantity = { cholesterol = it }
                    )

                    Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

                    QuantityTextField(
                        label = "Saturated Fats (mg)",
                        quantity = fatsSaturated,
                        updateQuantity = { fatsSaturated = it }
                    )

                    Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))
                }
            }

            // Pie chart to show dish macros

            Button(
                onClick = {
                    if (isEmpty(
                            imagePath,
                            dishName,
                            calories,
                            servingSize
                        )
                    ) {
                        Toast.makeText(
                            context,
                            "Please Fill The Required Fields!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        foodItemsViewModel.saveFoodItem(
                            FoodItemEntity(
                                imageStr = imagePath,
                                date = "-1",
                                month = "-1",
                                servings = -99,
                                hour = -1,
                                minutes = -1,
                                seconds = -1,
                                isConsumed = false,
                                isCreated = true,
                                foodItemDetails = com.sparshchadha.workout_app.features.food.data.remote.dto.food_api.FoodItem(
                                    calories = calories.getDouble(),
                                    carbohydrates_total_g = carbs.getDouble(),
                                    fat_total_g = fatsTotal.getDouble(),
                                    fat_saturated_g = fatsSaturated.getDouble(),
                                    cholesterol_mg = cholesterol.getDouble().toInt(),
                                    sodium_mg = sodium.getDouble().toInt(),
                                    potassium_mg = potassium.getDouble().toInt(),
                                    protein_g = protein.getDouble(),
                                    sugar_g = sugar.getDouble(),
                                    fiber_g = fiber.getDouble(),
                                    serving_size_g = servingSize.getDouble(),
                                    name = dishName
                                )
                            )
                        )
                    }
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

private fun String.getDouble(): Double {
    if (this.isBlank()) return -1.0
    return this.toDouble()
}

@Composable
private fun QuantityTextField(
    label: String,
    quantity: String,
    updateQuantity: (String) -> Unit,
    isMandatory: Boolean = false
) {
    OutlinedTextField(
        value = quantity,
        onValueChange = { updateQuantity(it) },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = if (isMandatory) ColorsUtil.noAchievementColor else ColorsUtil.unselectedBottomBarIconColor,
            focusedBorderColor = if (isMandatory) ColorsUtil.noAchievementColor else ColorsUtil.unselectedBottomBarIconColor,
            unfocusedBorderColor = if (isMandatory) ColorsUtil.noAchievementColor else ColorsUtil.unselectedBottomBarIconColor,
            errorBorderColor = if (isMandatory) ColorsUtil.noAchievementColor else ColorsUtil.unselectedBottomBarIconColor,
            focusedLabelColor = ColorsUtil.primaryTextColor,
            disabledLabelColor = ColorsUtil.primaryTextColor,
            unfocusedLabelColor = ColorsUtil.primaryTextColor,
            errorLabelColor = ColorsUtil.primaryTextColor
        )
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

@Composable
private fun Header(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(4f)
                .padding(Dimensions.MEDIUM_PADDING),
            textAlign = TextAlign.Start,
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            fontWeight = FontWeight.Bold
        )

        Icon(imageVector = icon, contentDescription = null, tint = ColorsUtil.primaryPurple)
    }
}


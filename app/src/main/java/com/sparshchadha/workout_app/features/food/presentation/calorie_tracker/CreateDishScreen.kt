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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
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
    var fats by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

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

    }

    Column(
        modifier = Modifier
            .padding(Dimensions.LARGE_PADDING)
            .verticalScroll(rememberScrollState())
    ) {
        // TODO-  Pick an image


        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        // Dish name
        OutlinedTextField(
            value = dishName,
            onValueChange = { dishName = it },
            label = { Text("Dish Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        // Optionals - Carbs, Protein, Fats
        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carbs (g)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        OutlinedTextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Protein (g)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        OutlinedTextField(
            value = fats,
            onValueChange = { fats = it },
            label = { Text("Fats (g)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        // When adding dish, dynamic way to add quantity like grams, cups, slices etc
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        // Pie chart to show dish macros

        // calories in kcal
        Text("Calories: TBD kcal", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)

        Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

        Button(
            onClick = {
//                      foodItemsViewModel.saveFoodItem()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Dish")
        }
    }
}

package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishesOptionsBottomSheet(
    hideBottomSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        }
    ) {
        // Create dish
        //
    }
}
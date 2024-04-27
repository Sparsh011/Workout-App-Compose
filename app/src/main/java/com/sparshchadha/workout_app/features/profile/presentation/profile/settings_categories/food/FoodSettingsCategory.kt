package com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.food

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.sparshchadha.workout_app.features.profile.presentation.profile.shared.SettingsCategory
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun FoodSettingsCategory(
    navigateToRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.SMALL_PADDING)
            .clip(RoundedCornerShape(Dimensions.SMALL_PADDING))
            .background(ColorsUtil.bottomBarColor)
    ) {
        for (i in 0 until HelperFunctions.settingsForCalorieTracker().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForCalorieTracker()[i],
                onClick = {
                    handleCaloriesTrackerItemClick(
                        clickedItem = HelperFunctions.settingsForCalorieTracker()[i],
                        navigateToScreen = { route ->
                            navigateToRoute(route)
                        }
                    )
                },
                verticalLineColor = ColorsUtil.primaryBlue,
//                    showDivider = i != HelperFunctions.settingsForCalorieTracker().size - 1
            )
        }
    }
}

fun handleCaloriesTrackerItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Food" -> {

        }

        "Activity" -> {

        }

        "Goals" -> {

        }

        "Saved Food Items" -> {
            navigateToScreen("SavedItemsScreen/calorieTracker")
        }

        "Your Dishes" -> {
            navigateToScreen("SavedItemsScreen/calorieTracker")
        }
    }
}
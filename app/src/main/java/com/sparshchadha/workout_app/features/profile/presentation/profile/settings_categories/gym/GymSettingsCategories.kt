package com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.sparshchadha.workout_app.features.profile.presentation.profile.shared.SettingsCategory
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun GymSettingsCategories(
    navigateToRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clip(RoundedCornerShape(SMALL_PADDING))
            .background(bottomBarColor)
    ) {
        for (i in 0 until HelperFunctions.settingsForGym().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForGym()[i],
                onClick = {
                    handleGymItemClick(
                        clickedItem = HelperFunctions.settingsForGym()[i],
                        navigateToScreen = { route ->
                            navigateToRoute(route)
                        }
                    )
                },
                verticalLineColor = ColorsUtil.primaryBlue,
//                    showDivider = i != HelperFunctions.settingsForGym().size - 1
            )
        }
    }
}


fun handleGymItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Workouts" -> {
            navigateToScreen(UtilityScreenRoutes.GymExercisesPerformed.route)
        }

        "Activity" -> {
            navigateToScreen(UtilityScreenRoutes.GymActivityScreen.route)
        }

        "Personal Records" -> {
            navigateToScreen(UtilityScreenRoutes.PersonalRecordsScreen.route)
        }

        "Goals" -> {

        }

        "Saved Exercises" -> {
            navigateToScreen("SavedItemsScreen/gym")
        }
    }
}
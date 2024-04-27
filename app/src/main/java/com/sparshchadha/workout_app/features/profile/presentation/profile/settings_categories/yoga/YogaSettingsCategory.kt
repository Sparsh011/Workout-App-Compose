package com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.yoga

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
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun YogaSettingsCategory(
    navigateToRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.SMALL_PADDING)
            .clip(RoundedCornerShape(Dimensions.SMALL_PADDING))
            .background(ColorsUtil.bottomBarColor)
    ) {
        for (i in 0 until HelperFunctions.settingsForYoga().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForYoga()[i],
                onClick = {
                    handleYogaItemClick(
                        clickedItem = HelperFunctions.settingsForYoga()[i],
                        navigateToScreen = { route ->
                            navigateToRoute(route)
                        }
                    )
                },
                verticalLineColor = ColorsUtil.primaryBlue,
//                    showDivider = i != HelperFunctions.settingsForYoga().size - 1
            )
        }
    }
}

fun handleYogaItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Workouts" -> {
            navigateToScreen(UtilityScreenRoutes.YogaPosesPerformed.route)
        }

        "Activity" -> {
            navigateToScreen(UtilityScreenRoutes.YogaActivityScreen.route)
        }

        "Goals" -> {

        }

        "Saved Poses" -> {
            navigateToScreen("SavedItemsScreen/yoga")
        }
    }
}
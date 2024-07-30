package com.sparshchadha.workout_app.features.food.presentation.calorie_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.food.domain.entities.FoodItemEntity
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.NoWorkoutPerformedOrFoodConsumed
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.Resource

@Composable
fun FoodItemsConsumed(
    foodItemsConsumed: Resource<List<FoodItemEntity>>?,
    navController: NavController,
    removeFoodItem: (FoodItemEntity) -> Unit,
    setDetailsFoodItemId: (Int) -> Unit
) {
    when (foodItemsConsumed) {
        is Resource.Error -> {
            ErrorDuringFetch(
                errorMessage = foodItemsConsumed.error?.message
                    ?: "Something Went Wrong!"
            )
        }

        is Resource.Loading -> {
            ShowLoadingScreen()
        }

        is Resource.Success -> {
            if (foodItemsConsumed.data.isNullOrEmpty()) {
                NoWorkoutPerformedOrFoodConsumed(
                    text = "No Food Items Consumed",
                    textSize = 20.nonScaledSp,
                    iconSize = Dimensions.PIE_CHART_SIZE
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                        .background(ColorsUtil.bottomBarColor)
                ) {
                    for (index in foodItemsConsumed.data.indices) {
                        FoodItem(
                            consumedFoodItem = foodItemsConsumed.data[index],
                            showFoodItemDetails = {
                                navController.navigate(route = UtilityScreenRoutes.FoodItemDetailsScreen.route)
                                setDetailsFoodItemId(foodItemsConsumed.data[index].id ?: -1)
                            },
                            removeFoodItem = {
                                removeFoodItem(it)
                            }
                        )
                    }
                }
            }
        }

        else -> {

        }
    }
}

@Composable
fun FoodItem(
    consumedFoodItem: FoodItemEntity,
    showFoodItemDetails: () -> Unit,
    removeFoodItem: (FoodItemEntity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.SMALL_PADDING)
            .clickable {
                showFoodItemDetails()
            }
            .background(ColorsUtil.bottomBarColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.SMALL_PADDING)
                .background(ColorsUtil.bottomBarColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            consumedFoodItem.foodItemDetails?.name?.let { itemName ->
                Text(
                    text = "${itemName.capitalize()}, ${consumedFoodItem.servings} servings",
                    color = ColorsUtil.primaryTextColor,
                    modifier = Modifier
                        .padding(Dimensions.SMALL_PADDING)
                        .weight(3.75f),
                    fontSize = 16.nonScaledSp,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "${(consumedFoodItem.foodItemDetails?.calories ?: 0).toInt() * (consumedFoodItem.servings)} kcal",
                color = ColorsUtil.targetAchievedColor,
                modifier = Modifier
                    .padding(Dimensions.SMALL_PADDING)
                    .weight(1.25f),
                fontSize = 16.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(Dimensions.SMALL_PADDING))

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = ColorsUtil.noAchievementColor,
                modifier = Modifier.clickable {
                    removeFoodItem(consumedFoodItem)
                }
            )
        }
    }
}
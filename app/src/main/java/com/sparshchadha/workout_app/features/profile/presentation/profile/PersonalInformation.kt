package com.sparshchadha.workout_app.features.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun PersonalInformation(
    height: String,
    weight: String,
    bmi: String,
    gender: String,
    age: String,
    weightGoal: String,
    caloriesGoal: String,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
            .clip(RoundedCornerShape(SMALL_PADDING))
            .background(bottomBarColor)
    ) {
        HelperFunctions.getPersonalInfoCategories().forEachIndexed { index, category ->
            var categoryValue = ""
            when (index) {
                0 -> {
                    categoryValue = "$height cm"
                }

                1 -> {
                    categoryValue = "$weight kg"
                }

                2 -> {
                    categoryValue = gender
                }

                3 -> {
                    categoryValue = bmi
                }

                4 -> {
                    categoryValue = age
                }

                5 -> {
                    categoryValue = "$weightGoal kg"
                }

                6 -> {
                    categoryValue = caloriesGoal
                }
            }

            if (categoryValue == bmi) {
                PersonalInfoCategory(
                    category = category,
                    categoryValue = categoryValue,
                    categoryValueColor = if (bmi.toDouble() > 25 || bmi.toDouble() < 18) noAchievementColor else targetAchievedColor
                )
            } else {
                PersonalInfoCategory(
                    category = category,
                    categoryValue = categoryValue,
                    onClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun PersonalInfoCategory(
    category: String,
    categoryValue: String,
    categoryValueColor: Color = primaryTextColor,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SMALL_PADDING)
            .clickable {
                onClick(category)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = category,
            fontSize = 16.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(start = MEDIUM_PADDING, top = MEDIUM_PADDING, bottom = MEDIUM_PADDING)
                .weight(4f)
        )

        Text(
            text = categoryValue,
            color = categoryValueColor,
            modifier = Modifier
                .padding(end = MEDIUM_PADDING, top = MEDIUM_PADDING, bottom = MEDIUM_PADDING)
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 16.nonScaledSp,
            maxLines = 1
        )
    }
}
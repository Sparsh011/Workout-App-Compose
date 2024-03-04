package com.sparshchadha.workout_app.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun NoWorkoutPerformedOrFoodConsumed(
    text: String = "",
    localPaddingValues: PaddingValues = PaddingValues(),
    textSize: TextUnit = 24.nonScaledSp,
    iconSize: Dp = Dimensions.PIE_CHART_SIZE + Dimensions.LARGE_PADDING
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = localPaddingValues.calculateTopPadding())
            .background(ColorsUtil.scaffoldBackgroundColor),
        horizontalAlignment = CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.no_result_found),
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.size(iconSize)
        )

        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

        Text(
            text = text,
            color = ColorsUtil.primaryTextColor,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(Dimensions.LARGE_PADDING),
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )
    }
}

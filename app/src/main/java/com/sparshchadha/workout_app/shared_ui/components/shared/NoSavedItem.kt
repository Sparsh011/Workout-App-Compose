package com.sparshchadha.workout_app.shared_ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PIE_CHART_SIZE
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun NoSavedItem(
    text: String = "No Saved Item"
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.no_result_found),
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.size(PIE_CHART_SIZE + LARGE_PADDING)
        )

        Text(
            text = text,
            color = primaryTextColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 24.nonScaledSp
        )
    }
}
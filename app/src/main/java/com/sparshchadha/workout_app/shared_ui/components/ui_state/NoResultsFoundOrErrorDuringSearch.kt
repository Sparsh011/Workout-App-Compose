package com.sparshchadha.workout_app.shared_ui.components.ui_state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun NoResultsFoundOrErrorDuringSearch(
    globalPaddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
    message: String = "",
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = globalPaddingValues.calculateBottomPadding(),
                top = localPaddingValues.calculateTopPadding()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.no_result_found),
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.size(Dimensions.PIE_CHART_SIZE + Dimensions.LARGE_PADDING)
        )

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = primaryTextColor,
                modifier = Modifier
                    .padding(Dimensions.LARGE_PADDING)
                    .fillMaxWidth(),
                fontSize = 30.nonScaledSp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = "No Results Found!",
                color = primaryTextColor,
                modifier = Modifier
                    .padding(Dimensions.LARGE_PADDING)
                    .fillMaxWidth(),
                fontSize = 24.nonScaledSp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
package com.sparshchadha.workout_app.shared_ui.components.ui_state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions

@Composable
fun ErrorDuringFetch(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.no_result_found),
            contentDescription = null,
            tint = ColorsUtil.noAchievementColor,
            modifier = Modifier.size(Dimensions.PIE_CHART_SIZE + Dimensions.LARGE_PADDING)
        )

        Text(
            text = "Unable To Find Exercises!\n $errorMessage",
            color = Color.Black,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
    }
}
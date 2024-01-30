package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.sparshchadha.workout_app.util.ColorsUtil

@Composable
fun NoWorkoutPerformedOrFoodConsumed(
    text: String,
    composition: LottieComposition?,
    progress: Float,
    localPaddingValues: PaddingValues = PaddingValues(),
    globalPaddingValues: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = localPaddingValues.calculateTopPadding())
    ) {
        LottieAnimation(
            composition = composition,
            progress = {
                progress
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text,
            color = ColorsUtil.primaryDarkTextColor,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

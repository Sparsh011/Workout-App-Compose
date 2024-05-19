package com.sparshchadha.workout_app.ui.components.ui_state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun NoInternetScreen(
    modifier: Modifier
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_internet_anim))
        val animProgress by animateLottieCompositionAsState(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = lottieComposition,
            progress = { animProgress },
            modifier = Modifier.size(Dimensions.PIE_CHART_SIZE),
        )

        Text(
            text = "Please Check Your Internet Connection and Try Again!",
            color = ColorsUtil.primaryTextColor,
            fontSize = 16.nonScaledSp,
            textAlign = TextAlign.Center
        )
    }
}
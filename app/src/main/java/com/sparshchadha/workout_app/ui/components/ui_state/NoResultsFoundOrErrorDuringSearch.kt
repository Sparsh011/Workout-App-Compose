package com.sparshchadha.workout_app.ui.components.ui_state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
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
            )
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_results_found_animation))
        val progress by animateLottieCompositionAsState(composition)

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.padding(Dimensions.LARGE_PADDING)
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
                fontSize = 30.nonScaledSp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
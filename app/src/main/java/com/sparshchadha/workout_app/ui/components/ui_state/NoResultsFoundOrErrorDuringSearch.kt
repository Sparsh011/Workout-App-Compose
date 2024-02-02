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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R

@Composable
fun NoResultsFoundOrErrorDuringSearch(paddingValues: PaddingValues, localPaddingValues: PaddingValues, errorMessage: String = "") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding(), top = localPaddingValues.calculateTopPadding())
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_results_found_animation))
        val progress by animateLottieCompositionAsState(composition)

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.padding(20.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = "Error $errorMessage",
                color = Color.Black,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = "No Results Found!",
                color = Color.Black,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
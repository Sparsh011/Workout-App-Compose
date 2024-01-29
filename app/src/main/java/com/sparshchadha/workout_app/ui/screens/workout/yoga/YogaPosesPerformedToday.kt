package com.sparshchadha.workout_app.ui.screens.workout.yoga

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.entities.YogaEntity
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel


private const val TAG = "YogaPosesPerformedToday"
@Composable
fun YogaPosesPerformedToday(
    yogaPosesPerformedToday: List<YogaEntity>?,
    uiEventState: WorkoutViewModel.UIEvent?,
) {
    if (uiEventState != null) {
        HandleUIEventState(
            event = uiEventState,
            yogaPosesPerformedToday = yogaPosesPerformedToday
        )
    }
}

@Composable
fun HandleUIEventState(
    event: WorkoutViewModel.UIEvent,
    yogaPosesPerformedToday: List<YogaEntity>?,
) {
    when (event) {
        is WorkoutViewModel.UIEvent.ShowLoader -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga_animation))
            val progress by animateLottieCompositionAsState(composition)

            ShowLoadingScreen(
                composition = composition, progress = progress
            )
        }

        is WorkoutViewModel.UIEvent.HideLoaderAndShowResponse -> {
            PopulateSavedYogaPoses(
                yogaPosesPerformedToday = yogaPosesPerformedToday
            )
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
}

@Composable
fun PopulateSavedYogaPoses(
    yogaPosesPerformedToday: List<YogaEntity>?,
) {
    Log.e(TAG, "PopulateSavedYogaPoses: $yogaPosesPerformedToday")
    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ) {
        items(yogaPosesPerformedToday ?: emptyList()) { yogaEntity ->
            YogaEntityItem(yogaEntity = yogaEntity)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaEntityItem(yogaEntity: YogaEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            // Handle item click
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Date: ${yogaEntity.date} ${yogaEntity.month}",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sets Performed: ${yogaEntity.setsPerformed}")
            Spacer(modifier = Modifier.height(8.dp))
            yogaEntity.yogaPoseDetails?.let { pose ->
                Text(
                    text = "Pose: ${pose.english_name}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Difficulty Level: ${pose.difficulty_level}")
                Spacer(modifier = Modifier.height(8.dp))
                // Add more details as needed
            }
        }
    }
}

package com.sparshchadha.workout_app.ui.screens.workout.yoga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.screens.calorie_tracker.CaloriesConsumedAndSliderComposable
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaPosesScreen(
    workoutViewModel: WorkoutViewModel,
) {
    val yogaPoses = workoutViewModel.yogaPoses.value

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga_animation))
    val progress by animateLottieCompositionAsState(composition)

    if (yogaPoses != null) {
        LazyColumn(
            modifier = Modifier.background(Color.White)
        ) {
            items(yogaPoses.poses) { pose ->
                var showBottomSheetWithAllDetails by remember {
                    mutableStateOf(false)
                }

                val sheetState = rememberModalBottomSheetState()

                YogaPoseItemComposable(
                    englishName = pose.english_name,
                    description = pose.pose_description,
                    sanskritName = pose.sanskrit_name,
                    benefits = pose.pose_benefits,
                    poseImage = pose.url_png,
                    shouldShowBottomSheetWithDetails = showBottomSheetWithAllDetails,
                    toggleBottomSheetWithDetails = {
                        showBottomSheetWithAllDetails = it
                    },
                    sheetState = sheetState
                )
            }
        }
    } else {
        Column {
            LottieAnimation(
                composition = composition,
                progress = {
                    progress
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaPoseItemComposable(
    englishName: String,
    sanskritName: String,
    description: String,
    benefits: String,
    poseImage: String,
    shouldShowBottomSheetWithDetails: Boolean,
    toggleBottomSheetWithDetails: (Boolean) -> Unit,
    sheetState: SheetState,
) {

    if (shouldShowBottomSheetWithDetails) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                toggleBottomSheetWithDetails(false)
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            AsyncImage(
                model = poseImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
            )

            Text(
                text = "$englishName (in Sanskrit $sanskritName)",
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.TITLE_SIZE,
                color = ColorsUtil.primaryBackgroundColor,
                modifier = Modifier.align(CenterHorizontally)
            )

            Text(text = "Benefits : \n", fontWeight = FontWeight.Bold)

            Text(
                text = benefits,
                color = ColorsUtil.primaryBackgroundColor,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = "Description : \n", fontWeight = FontWeight.Bold)

            Text(
                text = description,
                color = ColorsUtil.primaryBackgroundColor,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                toggleBottomSheetWithDetails(true)
            }
    ) {
        AsyncImage(
            model = poseImage,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
        )

        Column {
            Text(
                text = "$englishName (in Sanskrit $sanskritName)",
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.TITLE_SIZE,
                color = ColorsUtil.primaryBackgroundColor
            )

            Text(
                text = benefits,
                color = ColorsUtil.primaryBackgroundColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

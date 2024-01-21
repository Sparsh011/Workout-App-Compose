package com.sparshchadha.workout_app.ui.screens.workout.yoga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaPosesScreen(
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
) {
    val yogaPoses = workoutViewModel.yogaPoses.value
    val difficultyLevel = workoutViewModel.getCurrentYogaDifficultyLevel()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga_animation))
    val progress by animateLottieCompositionAsState(composition)

    if (yogaPoses != null) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(horizontal = 10.dp)
                            .clickable {
                                navController.popBackStack(BottomBarScreen.WorkoutScreen.route, inclusive = false)
                            }
                    )

                    Text(
                        text = "${difficultyLevel.lowercase().capitalize()} Yoga Poses",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(0.8f)
                            .align(CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .padding(paddingValues = it),
                horizontalAlignment = CenterHorizontally
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
                        toggleBottomSheetWithDetails = { toggle ->
                            showBottomSheetWithAllDetails = toggle
                        },
                        sheetState = sheetState
                    )
                }
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
            windowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.White
        ) {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = poseImage,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(CenterHorizontally)
                )

                Text(
                    text = "$englishName ($sanskritName)",
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimensions.TITLE_SIZE,
                    color = ColorsUtil.primaryBackgroundColor,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(20.dp)
                )

                Text(
                    text = "Benefits ",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    color = Color.Black,
                    fontSize = Dimensions.TITLE_SIZE
                )

                Text(
                    text = benefits,
                    color = ColorsUtil.primaryBackgroundColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )

                Text(
                    text = "Description ",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = Dimensions.TITLE_SIZE,
                    color = Color.Black
                )

                Text(
                    text = description,
                    color = ColorsUtil.primaryBackgroundColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
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
                text = "$englishName ($sanskritName)",
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

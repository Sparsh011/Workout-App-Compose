package com.sparshchadha.workout_app.ui.screens.workout.yoga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.yoga.Pose
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.components.PickNumberOfSetsOrQuantity
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.rememberPickerState
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.HelperFunctions.getCurrentDateAndMonth
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel

@Composable
fun YogaPosesScreen(
    yogaPoses: YogaPosesDto?,
    navController: NavController,
    difficultyLevel: String,
    uiEventState: WorkoutViewModel.UIEvent?,
    saveYogaPose: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
) {

    uiEventState?.let { event ->
        HandleYogaScreenUIEvents(
            event = event,
            yogaPoses = yogaPoses,
            navController = navController,
            difficultyLevel = difficultyLevel,
            saveYogaPose = saveYogaPose,
            globalPaddingValues = globalPaddingValues
        )
    }
}

@Composable
fun HandleYogaScreenUIEvents(
    event: WorkoutViewModel.UIEvent,
    yogaPoses: YogaPosesDto?,
    navController: NavController,
    difficultyLevel: String,
    saveYogaPose: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
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
            if (yogaPoses != null) {
                PopulateYogaPoses(
                    yogaPoses = yogaPoses,
                    navController = navController,
                    difficultyLevel = difficultyLevel,
                    saveYogaPose = saveYogaPose,
                    globalPaddingValues = globalPaddingValues
                )
            } else {
                ErrorDuringFetch(errorMessage = "")
            }
        }

        is WorkoutViewModel.UIEvent.ShowError -> {
            ErrorDuringFetch(errorMessage = event.errorMessage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopulateYogaPoses(
    yogaPoses: YogaPosesDto,
    navController: NavController,
    difficultyLevel: String,
    saveYogaPose: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "${difficultyLevel.lowercase().capitalize()} Yoga Poses",
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = BottomBarScreen.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }
    ) { localPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(top = localPaddingValues.calculateTopPadding(), bottom = globalPaddingValues.calculateBottomPadding()),
            horizontalAlignment = CenterHorizontally
        ) {

            items(yogaPoses.poses) { pose ->
                var showBottomSheetWithAllDetails by remember {
                    mutableStateOf(false)
                }

                val sheetState = rememberModalBottomSheetState()

                YogaPose(
                    pose = pose,
                    shouldShowBottomSheetWithDetails = showBottomSheetWithAllDetails,
                    toggleBottomSheetWithDetails = { toggle ->
                        showBottomSheetWithAllDetails = toggle
                    },
                    sheetState = sheetState,
                    saveYogaPose = saveYogaPose
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YogaPose(
    pose: Pose,
    shouldShowBottomSheetWithDetails: Boolean,
    toggleBottomSheetWithDetails: (Boolean) -> Unit,
    sheetState: SheetState,
    saveYogaPose: (YogaEntity) -> Unit,
) {

    if (shouldShowBottomSheetWithDetails) {
        ShowYogaPoseDetailsInModalBottomSheet(
            sheetState = sheetState, toggleBottomSheetWithDetails = toggleBottomSheetWithDetails, pose = pose
        )
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
            model = pose.url_png, contentDescription = null, modifier = Modifier.padding(10.dp)
        )

        Column {
            Text(
                text = "${pose.english_name} (${pose.sanskrit_name})",
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.TITLE_SIZE,
                color = ColorsUtil.primaryDarkTextColor
            )

            Text(
                text = pose.pose_benefits, color = ColorsUtil.primaryDarkTextColor, maxLines = 3, overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            var showPickSetsBottomSheet by remember {
                mutableStateOf(false)
            }

            Button(
                onClick = {
                    showPickSetsBottomSheet = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorsUtil.primaryDarkTextColor
                ),
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Text(text = "Add", color = Color.White)
            }

            if (showPickSetsBottomSheet) {
                ShowPickSetsBottomSheet(
                    pose = pose,
                    saveYogaPose = saveYogaPose,
                    hidePickSetsBottomSheet = {
                        showPickSetsBottomSheet = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPickSetsBottomSheet(pose: Pose, saveYogaPose: (YogaEntity) -> Unit, hidePickSetsBottomSheet: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            hidePickSetsBottomSheet()
        },
        windowInsets = WindowInsets(0, 0, 0, 10),
        containerColor = Color.White,
        sheetState = sheetState
    ) {
        val valuesPickerState = rememberPickerState()

        Text(
            buildAnnotatedString {
                append("Select ")
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.primaryDarkTextColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Sets Performed!")
                }
            },
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Start,
            color = ColorsUtil.primaryDarkTextColor,
        )

        PickNumberOfSetsOrQuantity(
            items = HelperFunctions.getNumberOfSetsOrQuantity(),
            state = valuesPickerState,
            modifier = Modifier.align(CenterHorizontally),
            textModifier = Modifier.padding(15.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val dateAndMonth = getCurrentDateAndMonth()
                saveYogaPose(
                    YogaEntity(
                        date = dateAndMonth.first.toString(),
                        month = dateAndMonth.second,
                        setsPerformed = valuesPickerState.selectedItem.toInt(),
                        yogaPoseDetails = pose
                    )
                )
                hidePickSetsBottomSheet()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorsUtil.primaryDarkTextColor
            ),
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            if (valuesPickerState.selectedItem == "1" ) {
                Text(text = "Add ${valuesPickerState.selectedItem} Set", color = Color.White)
            } else {
                Text(text = "Add ${valuesPickerState.selectedItem} Sets", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowYogaPoseDetailsInModalBottomSheet(
    sheetState: SheetState,
    toggleBottomSheetWithDetails: (Boolean) -> Unit,
    pose: Pose,
) {
    ModalBottomSheet(
        sheetState = sheetState, onDismissRequest = {
            toggleBottomSheetWithDetails(false)
        }, windowInsets = WindowInsets(0, 0, 0, 0), containerColor = Color.White
    ) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = pose.url_png, contentDescription = null, modifier = Modifier
                    .padding(10.dp)
                    .align(CenterHorizontally)
            )

            Text(
                text = "${pose.english_name} (${pose.sanskrit_name})",
                fontWeight = FontWeight.Bold,
                fontSize = Dimensions.TITLE_SIZE,
                color = ColorsUtil.primaryDarkTextColor,
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
                text = pose.pose_benefits,
                color = ColorsUtil.primaryDarkTextColor,
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
                text = pose.pose_description,
                color = ColorsUtil.primaryDarkTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.data.local.room_db.entities.YogaEntity
import com.sparshchadha.workout_app.data.remote.dto.yoga.Pose
import com.sparshchadha.workout_app.data.remote.dto.yoga.YogaPosesDto
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.components.PickNumberOfSetsOrQuantity
import com.sparshchadha.workout_app.ui.components.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.rememberPickerState
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryLightGray
import com.sparshchadha.workout_app.util.Dimensions.DESCRIPTION_SIZE
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.TITLE_SIZE
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.HelperFunctions.getCurrentDateAndMonth
import com.sparshchadha.workout_app.util.HelperFunctions.noRippleClickable
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
            PopulateYogaPoses(
                yogaPoses = YogaPosesDto(difficultyLevel, -1, emptyList()),
                navController = navController,
                difficultyLevel = difficultyLevel,
                saveYogaPose = saveYogaPose,
                globalPaddingValues = globalPaddingValues,
                isLoading = true,
                topBarDescription = ""
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
    isLoading: Boolean = false,
    topBarDescription: String = "${difficultyLevel.lowercase().capitalize()} Yoga Poses",
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = topBarDescription,
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = BottomBarScreen.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }
    ) { localPaddingValues ->
        if (isLoading) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.yoga_animation))
            val progress by animateLottieCompositionAsState(composition)

            ShowLoadingScreen(
                composition = composition, progress = progress
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding()
                    ),
                horizontalAlignment = CenterHorizontally
            ) {

                items(yogaPoses.poses.size) { index ->
                    var showBottomSheetWithAllDetails by remember {
                        mutableStateOf(false)
                    }

                    val sheetState = rememberModalBottomSheetState()

                    YogaPose(
                        pose = yogaPoses.poses[index],
                        shouldShowBottomSheetWithDetails = showBottomSheetWithAllDetails,
                        toggleBottomSheetWithDetails = { toggle ->
                            showBottomSheetWithAllDetails = toggle
                        },
                        sheetState = sheetState,
                        saveYogaPose = saveYogaPose,
                        showDivider = index != yogaPoses.poses.size - 1
                    )
                }
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
    showDivider: Boolean,
) {

    var showPickSetsBottomSheet by remember {
        mutableStateOf(false)
    }

    if (shouldShowBottomSheetWithDetails) {
        ShowYogaPoseDetailsInModalBottomSheet(
            sheetState = sheetState,
            toggleBottomSheetWithDetails = toggleBottomSheetWithDetails,
            pose = pose,
            showPickSetBottomSheet = {
                toggleBottomSheetWithDetails(false)
                showPickSetsBottomSheet = true
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING)
            .clickable {
                toggleBottomSheetWithDetails(true)
            }
    ) {
        AsyncImage(
            model = pose.url_png,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = MEDIUM_PADDING)
        )

        Column {
            Text(
                text = pose.english_name,
                fontWeight = Bold,
                fontSize = TITLE_SIZE,
                color = primaryDarkTextColor
            )

            Text(
                text = pose.pose_benefits,
                color = primaryDarkTextColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = DESCRIPTION_SIZE
            )

            Spacer(modifier = Modifier.height(LARGE_PADDING))

            Text(
                text = "Add",
                color = primaryBlue,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .noRippleClickable {
                        showPickSetsBottomSheet = true
                    },
                fontSize = 15.nonScaledSp,
                fontWeight = Bold
            )
//            }

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


    if (showDivider) {
        CustomDivider(dividerColor = primaryLightGray)
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
                        color = primaryDarkTextColor,
                        fontWeight = Bold
                    )
                ) {
                    append("Sets")
                }
            },
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING),
            textAlign = TextAlign.Start,
            color = primaryDarkTextColor,
        )

        PickNumberOfSetsOrQuantity(
            items = HelperFunctions.getNumberOfSetsOrQuantity(),
            state = valuesPickerState,
            modifier = Modifier.align(CenterHorizontally),
            textModifier = Modifier.padding(LARGE_PADDING)
        )

        Spacer(modifier = Modifier.height(LARGE_PADDING))

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
                containerColor = primaryDarkTextColor
            ),
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(horizontal = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            if (valuesPickerState.selectedItem == "1") {
                Text(text = "Add ${valuesPickerState.selectedItem} Set", color = Color.White)
            } else {
                Text(text = "Add ${valuesPickerState.selectedItem} Sets", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(LARGE_PADDING))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowYogaPoseDetailsInModalBottomSheet(
    sheetState: SheetState,
    toggleBottomSheetWithDetails: (Boolean) -> Unit,
    pose: Pose,
    showPickSetBottomSheet: () -> Unit = {},
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
                    .padding(MEDIUM_PADDING)
                    .align(CenterHorizontally)
            )

            Text(
                text = "${pose.english_name} (${pose.sanskrit_name})",
                fontWeight = Bold,
                fontSize = TITLE_SIZE,
                color = primaryDarkTextColor,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(LARGE_PADDING)
            )

            Text(
                text = "Benefits ",
                fontWeight = Bold,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
                color = Color.Black,
                fontSize = TITLE_SIZE
            )

            Text(
                text = pose.pose_benefits,
                color = primaryDarkTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
            )

            Text(
                text = "Description ",
                fontWeight = Bold,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
                fontSize = TITLE_SIZE,
                color = Color.Black
            )

            Text(
                text = pose.pose_description,
                color = primaryDarkTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
            )

            Button(
                onClick = {
                    showPickSetBottomSheet()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryDarkTextColor
                ),
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(horizontal = LARGE_PADDING)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add",
                    color = Color.White,
                    modifier = Modifier
                        .noRippleClickable {
                            showPickSetBottomSheet()
                        },
                    fontSize = 15.nonScaledSp,
                    fontWeight = Bold
                )
            }

            Spacer(modifier = Modifier.height(LARGE_PADDING))
        }
    }
}

package com.sparshchadha.workout_app.features.yoga.presentation.screens

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
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.Pose
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.YogaPosesDto
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.ui.components.bottom_bar.ScreenRoutes
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.components.shared.PickNumberOfSetsOrQuantity
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.ui.components.shared.rememberPickerState
import com.sparshchadha.workout_app.ui.components.ui_state.ErrorDuringFetch
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.DESCRIPTION_SIZE
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Dimensions.TITLE_SIZE
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.HelperFunctions.getCurrentDateAndMonth
import com.sparshchadha.workout_app.util.HelperFunctions.noRippleClickable
import com.sparshchadha.workout_app.util.Resource

@Composable
fun YogaPosesScreen(
    navController: NavController,
    globalPaddingValues: PaddingValues,
    yogaViewModel: YogaViewModel
) {
    val difficultyLevel = yogaViewModel.getCurrentYogaDifficultyLevel()
    val yogaPoses by yogaViewModel.yogaPosesFromApi

    HandleYogaScreenUIEvents(
        yogaPoses = yogaPoses,
        navController = navController,
        difficultyLevel = difficultyLevel,
        savePerformedYogaPose = { yogaEntity ->
            yogaViewModel.saveYogaPose(
                yogaEntity
            )
        },
        globalPaddingValues = globalPaddingValues
    ) {
        yogaViewModel.addYogaPoseToSaved(
            HelperFunctions.getYogaPoseWithNegatives(it)
        )
    }
}

@Composable
fun HandleYogaScreenUIEvents(
    yogaPoses: Resource<YogaPosesDto>?,
    navController: NavController,
    difficultyLevel: String,
    savePerformedYogaPose: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
    saveYogaPose: (Pose) -> Unit
) {
    when (yogaPoses) {
        is Resource.Loading -> {
            PopulateYogaPoses(
                yogaPoses = YogaPosesDto(difficultyLevel, -1, emptyList()),
                navController = navController,
                difficultyLevel = difficultyLevel,
                savePerformedYogaPose = savePerformedYogaPose,
                globalPaddingValues = globalPaddingValues,
                isLoading = true,
                topBarDescription = "",
                saveYogaPose = {}
            )
        }

        is Resource.Success -> {
            if (yogaPoses.data != null) {
                PopulateYogaPoses(
                    yogaPoses = yogaPoses.data,
                    navController = navController,
                    difficultyLevel = difficultyLevel,
                    savePerformedYogaPose = savePerformedYogaPose,
                    globalPaddingValues = globalPaddingValues,
                    saveYogaPose = saveYogaPose
                )
            } else {
                ErrorDuringFetch(errorMessage = "")
            }
        }

        is Resource.Error -> {
            ErrorDuringFetch(errorMessage = yogaPoses.error?.message ?: "Unable To Get Poses")
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopulateYogaPoses(
    yogaPoses: YogaPosesDto,
    navController: NavController,
    difficultyLevel: String,
    savePerformedYogaPose: (YogaEntity) -> Unit,
    globalPaddingValues: PaddingValues,
    isLoading: Boolean = false,
    topBarDescription: String = "${difficultyLevel.lowercase().capitalize()} Yoga Poses",
    saveYogaPose: (Pose) -> Unit
) {
    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = topBarDescription,
                onBackButtonPressed = {
                    navController.popBackStack(
                        route = ScreenRoutes.WorkoutScreen.route,
                        inclusive = false
                    )
                }
            )
        }
    ) { localPaddingValues ->
        if (isLoading) {
            ShowLoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(scaffoldBackgroundColor)
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
                        savePerformedYogaPose = savePerformedYogaPose,
                        showDivider = index != yogaPoses.poses.size - 1,
                        saveYogaPose = saveYogaPose
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
    savePerformedYogaPose: (YogaEntity) -> Unit,
    showDivider: Boolean,
    saveYogaPose: (Pose) -> Unit
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
            },
            saveYogaPose = saveYogaPose
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
                .weight(1f)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pose.english_name,
                fontWeight = Bold,
                fontSize = TITLE_SIZE,
                color = primaryTextColor
            )

            Text(
                text = pose.pose_benefits,
                color = primaryTextColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = DESCRIPTION_SIZE
            )

            Spacer(modifier = Modifier.height(LARGE_PADDING))

            if (showPickSetsBottomSheet) {
                ShowPickSetsBottomSheet(
                    pose = pose,
                    saveYogaPose = savePerformedYogaPose,
                    hidePickSetsBottomSheet = {
                        showPickSetsBottomSheet = false
                    }
                )
            }
        }
    }

    if (showDivider) {
        CustomDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPickSetsBottomSheet(
    pose: Pose,
    saveYogaPose: (YogaEntity) -> Unit,
    hidePickSetsBottomSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            hidePickSetsBottomSheet()
        },
        windowInsets = WindowInsets(0, 0, 0, 10),
        containerColor = scaffoldBackgroundColor,
        sheetState = sheetState
    ) {
        val valuesPickerState = rememberPickerState()

        Text(
            buildAnnotatedString {
                append("Select ")
                withStyle(
                    style = SpanStyle(
                        color = primaryTextColor,
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
            color = primaryTextColor,
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
                containerColor = primaryPurple
            ),
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(start = LARGE_PADDING, bottom = Dimensions.BOTTOM_SHEET_BOTTOM_PADDING, end = LARGE_PADDING)
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
    saveYogaPose: (Pose) -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            toggleBottomSheetWithDetails(false)
        },
        containerColor = scaffoldBackgroundColor
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
                color = primaryTextColor,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(LARGE_PADDING)
            )

            Text(
                text = "Benefits ",
                fontWeight = Bold,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
                color = primaryTextColor,
                fontSize = TITLE_SIZE
            )

            Text(
                text = pose.pose_benefits,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
            )

            Text(
                text = "Description ",
                fontWeight = Bold,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
                fontSize = TITLE_SIZE,
                color = primaryTextColor,
            )

            Text(
                text = pose.pose_description,
                color = primaryTextColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = LARGE_PADDING, vertical = MEDIUM_PADDING),
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        showPickSetBottomSheet()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryPurple
                    ),
                    modifier = Modifier
                        .padding(horizontal = LARGE_PADDING + MEDIUM_PADDING)
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

                Button(
                    onClick = {
                        saveYogaPose(pose)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryPurple
                    ),
                    modifier = Modifier
                        .padding(
                            horizontal = LARGE_PADDING + MEDIUM_PADDING,
                            vertical = SMALL_PADDING
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        modifier = Modifier
                            .noRippleClickable {
                                showPickSetBottomSheet()
                            },
                        fontSize = 15.nonScaledSp,
                        fontWeight = Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(LARGE_PADDING))
        }
    }
}

package com.sparshchadha.workout_app.features.gym.presentation.gym

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.gym.domain.entities.PersonalRecordsEntity
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.shared_ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.shared_ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.BOTTOM_SHEET_BOTTOM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalRecordsScreen(
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues,
    navController: NavController
) {
    var showBottomSheetToAddPR by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getAllPR(category = "gym")
    }

    val prList by workoutViewModel.personalRecords.collectAsStateWithLifecycle()
    var prId by remember {
        mutableIntStateOf(-1)
    }

    var updatePr by remember {
        mutableStateOf(false)
    }

    if (showBottomSheetToAddPR) {
        if (updatePr) {
            PRModalBottomSheet(
                hideBottomSheet = {
                    showBottomSheetToAddPR = false
                    updatePr = false
                },
                addPR = { newPr ->
                    if (newPr.second) {
                        workoutViewModel.updatePR(newPr.first)
                    } else {
                        workoutViewModel.addPR(newPr.first)
                    }
                },
                prId = prId
            )
        } else {
            PRModalBottomSheet(
                hideBottomSheet = {
                    showBottomSheetToAddPR = false
                    updatePr = false
                },
                addPR = { newPr ->
                    if (newPr.second) {
                        workoutViewModel.updatePR(newPr.first)
                    } else {
                        workoutViewModel.addPR(newPr.first)
                    }
                }
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheetToAddPR = true
                },
                containerColor = primaryPurple,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Personal Records",
                onBackButtonPressed = {
                    navController.popBackStack()
                }
            )
        },
        modifier = Modifier.padding(bottom = globalPaddingValues.calculateBottomPadding()),
        containerColor = scaffoldBackgroundColor
    ) { localPaddingValues ->
        if (prList != null && prList!!.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = localPaddingValues.calculateTopPadding(),
                        bottom = globalPaddingValues.calculateBottomPadding()
                    )
                    .background(scaffoldBackgroundColor)
            ) {
                items(prList!!) {
                    PR(
                        record = it,
                        onEditClick = {
                            showBottomSheetToAddPR = true
                            updatePr = true
                            prId = it.id ?: -1
                        },
                        onDeleteClick = {
                            workoutViewModel.deletePR(it)
                        }
                    )
                }
            }
        } else {
            NoSavedItem(text = "No personal records added")
        }
    }
}

@Composable
fun PR(
    record: PersonalRecordsEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SMALL_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = record.exerciseName.capitalize(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.nonScaledSp,
            modifier = Modifier
                .weight(4.3f)
                .padding(horizontal = MEDIUM_PADDING),
            color = primaryTextColor
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = primaryTextColor,
            modifier = Modifier
                .clickable {
                    onEditClick()
                }
                .weight(0.7f)
                .padding(vertical = SMALL_PADDING)
        )
    }

    Spacer(modifier = Modifier.height(SMALL_PADDING))

    Text(
        text = "${record.weight} kg, ${record.reps} reps",
        color = primaryTextColor,
        modifier = Modifier.padding(horizontal = MEDIUM_PADDING + SMALL_PADDING),
        fontSize = 20.nonScaledSp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(SMALL_PADDING))

    if (record.optionalDescription.isNotBlank()) {
        Text(
            text = record.optionalDescription,
            color = primaryTextColor,
            modifier = Modifier.padding(
                start = MEDIUM_PADDING + SMALL_PADDING,
                end = MEDIUM_PADDING + SMALL_PADDING,
                bottom = SMALL_PADDING
            ),
            fontSize = 17.nonScaledSp
        )
    }

    Text(
        text = ("${record.date} ${record.month.lowercase().capitalize()} ${record.year}"),
        color = primaryTextColor,
        modifier = Modifier.padding(horizontal = MEDIUM_PADDING + SMALL_PADDING),
        fontSize = 14.nonScaledSp,
    )

    Spacer(modifier = Modifier.height(MEDIUM_PADDING))
    CustomDivider()
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PRModalBottomSheet(
    hideBottomSheet: () -> Unit,
    addPR: (Pair<PersonalRecordsEntity, Boolean>) -> Unit,
    prId: Int = -1
) {
    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        },
        containerColor = scaffoldBackgroundColor,
        windowInsets = WindowInsets.ime
    ) {
        var exerciseName by remember {
            mutableStateOf("")
        }
        var reps by remember {
            mutableStateOf("")
        }
        var weight by remember {
            mutableStateOf("")
        }
        var performedOn by remember {
            mutableStateOf("")
        }
        var optionalDescription by remember {
            mutableStateOf("")
        }

        PROutlinedTextField(
            setValue = {
                exerciseName = it
            },
            showErrorColor = exerciseName.isBlank(),
            label = "Exercise Name"
        )

        PROutlinedTextField(
            setValue = {
                weight = it
            },
            isText = false,
            showErrorColor = weight.isBlank(),
            label = "Weight Lifted (kg)"
        )

        PROutlinedTextField(
            setValue = {
                reps = it
            },
            isText = false,
            showErrorColor = reps.isBlank(),
            label = "Reps Performed"
        )

        PROutlinedTextField(
            setValue = {
                performedOn = it
            },
            showErrorColor = performedOn.isBlank(),
            label = "Performed On (dd/mm/yyyy)"
        )

        PROutlinedTextField(
            setValue = {
                optionalDescription = it
            },
            showErrorColor = false,
            label = "Description (optional)",
        )

        Button(
            onClick = {
                addPR(
                    Pair(
                        PersonalRecordsEntity(
                            exerciseName = exerciseName,
                            reps = reps.toInt(),
                            date = LocalDate.now().dayOfMonth.toString(),
                            month = LocalDate.now().month.toString(),
                            year = LocalDate.now().year.toString(),
                            weight = weight.toLong(),
                            optionalDescription = optionalDescription
                        ),
                        prId == -1
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = LARGE_PADDING + MEDIUM_PADDING, bottom = BOTTOM_SHEET_BOTTOM_PADDING, end = LARGE_PADDING + MEDIUM_PADDING, top = LARGE_PADDING + MEDIUM_PADDING),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryPurple
            )
        ) {
            Text(text = "Add", color = Color.White)
        }
    }
}

@Composable
fun PROutlinedTextField(
    setValue: (String) -> Unit,
    isText: Boolean = true,
    showErrorColor: Boolean,
    label: String
) {
    var value by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        label = {
            Text(
                text = label,
                color = primaryTextColor
            )
        },
        value = value,
        onValueChange = {
            if (isText) {
                setValue(it)
            } else {
                if (it.isDigitsOnly()) setValue(it)
            }
            value = it
        },
        keyboardOptions = if (isText) KeyboardOptions(keyboardType = KeyboardType.Text) else KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = primaryTextColor,
            unfocusedTextColor = primaryTextColor,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = if (showErrorColor) noAchievementColor else ColorsUtil.primaryBlue,
            unfocusedBorderColor = if (showErrorColor) noAchievementColor else ColorsUtil.primaryBlue,
            focusedBorderColor = if (showErrorColor) noAchievementColor else ColorsUtil.primaryBlue
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING)
    )
}

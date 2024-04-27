package com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.shared_ui.activity.components.GenderIcon
import com.sparshchadha.workout_app.shared_ui.activity.components.LandingPageOutlinedTextField
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun DialogToUpdateValue(
    showDialogToUpdateValueOf: String,
    profileViewModel: ProfileViewModel,
    hideDialog: () -> Unit,
    height: String,
    weightGoal: String,
    currentWeight: String,
    age: String,
    gender: String,
    caloriesGoal: String
) {
    when (showDialogToUpdateValueOf) {
        HelperFunctions.getPersonalInfoCategories()[0] -> {
            // Height
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = height,
                onConfirmClick = {
                    profileViewModel.saveHeight(it)
                },
                label = "Height (cm)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[1] -> {
            // "Weight",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = currentWeight,
                onConfirmClick = {
                    profileViewModel.saveCurrentWeight(it)
                },
                label = "Weight (kg)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[2] -> {
            //  "Gender",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = gender,
                onConfirmClick = {
                    profileViewModel.saveGender(it)
                },
                isGender = true,
                label = "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[3] -> {
            //  "BMI",
            return
        }

        HelperFunctions.getPersonalInfoCategories()[4] -> {
            //  "Age",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = age,
                onConfirmClick = {
                    profileViewModel.saveAge(it)
                },
                label = "Age",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[5] -> {
            //  "Weight Goal",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = weightGoal,
                onConfirmClick = {
                    profileViewModel.saveWeightGoal(it)
                },
                label = "Weight Goal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[6] -> {
            //  "Calories Goal"
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = caloriesGoal,
                onConfirmClick = {
                    profileViewModel.saveCaloriesGoal(it)
                },
                label = "Calories Goal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogToUpdate(
    hideDialog: () -> Unit,
    value: String,
    onConfirmClick: (String) -> Unit,
    isGender: Boolean = false,
    label: String,
    keyboardOptions: KeyboardOptions,
    isText: Boolean = false
) {
    AlertDialog(
        onDismissRequest = {
            hideDialog()
        }
    ) {
        if (!isGender) {
            var newValue by remember {
                mutableStateOf(value)
            }
            var isEmpty by remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(LARGE_PADDING))
                    .background(bottomBarColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    buildAnnotatedString {
                        append("Current ")
                        withStyle(
                            style = SpanStyle(
                                color = primaryTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(value)
                        }
                    },
                    color = primaryTextColor,
                    modifier = Modifier
                        .padding(LARGE_PADDING)
                )

                LandingPageOutlinedTextField(
                    label = label,
                    value = newValue,
                    onValueChange = {
                        if (isText) {
                            newValue = it
                        } else {
                            if (it.isDigitsOnly()) newValue = it
                        }
                        isEmpty = it.isBlank()
                    },
                    showErrorColor = isEmpty,
                    keyboardOptions = keyboardOptions,
                    isText = isText
                )

                Button(
                    onClick = {
                        if (newValue.isNotBlank()) {
                            onConfirmClick(newValue)
                            hideDialog()
                        }
                    },
                    modifier = Modifier
                        .padding(LARGE_PADDING)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorsUtil.primaryPurple
                    )
                ) {
                    Text(text = "Update", color = Color.White)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MEDIUM_PADDING)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bottomBarColor)
                    .padding(LARGE_PADDING)
            ) {
                GenderIcon(
                    resourceId = R.drawable.male,
                    selected = value == "Male",
                    onClick = {
                        onConfirmClick("Male")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.female,
                    selected = value == "Female",
                    onClick = {
                        onConfirmClick("Female")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.other_gender,
                    selected = value == "Other",
                    onClick = {
                        onConfirmClick("Other")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )
            }
        }
    }
}
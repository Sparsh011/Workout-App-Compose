package com.sparshchadha.workout_app.ui.activity.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun LandingPage(
    profileViewModel: ProfileViewModel,
    navigateToHomeScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Setup Your Profile",
            modifier = Modifier
                .fillMaxWidth()
                .background(statusBarColor)
                .padding(MEDIUM_PADDING),
            fontSize = 20.nonScaledSp,
            color = White
        )

        var ageStr by remember {
            mutableStateOf("")
        }

        var heightStr by remember {
            mutableStateOf("")
        }

        var weightStr by remember {
            mutableStateOf("")
        }

        var caloriesGoalStr by remember {
            mutableStateOf("")
        }

        var genderStr by remember {
            mutableStateOf("")
        }

        var weightGoalStr by remember {
            mutableStateOf("")
        }

        val context = LocalContext.current

        LandingPageOutlinedTextField(
            label = "Age",
            value = ageStr,
            onValueChange = {
                ageStr = it
            }
        )

        LandingPageOutlinedTextField(
            label = "Height (Cm)",
            value = heightStr,
            onValueChange = {
                heightStr = it
            }
        )

        LandingPageOutlinedTextField(
            label = "Weight (Kg)",
            value = weightStr,
            onValueChange = {
                weightStr = it
            }
        )

        LandingPageOutlinedTextField(
            label = "Calories Goal (kcal)",
            value = caloriesGoalStr,
            onValueChange = {
                caloriesGoalStr = it
            }
        )

        LandingPageOutlinedTextField(
            label = "Weight Goal (Kg)",
            value = weightGoalStr,
            onValueChange = {
                weightGoalStr = it
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MEDIUM_PADDING)
        ) {
            GenderIcon(
                resourceId = R.drawable.male,
                selected = genderStr == "Male",
                onClick = {
                    genderStr = "Male"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING)
            )

            GenderIcon(
                resourceId = R.drawable.female,
                selected = genderStr == "Female",
                onClick = {
                    genderStr = "Female"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING)
            )

            GenderIcon(
                resourceId = R.drawable.other_gender,
                selected = genderStr == "Other",
                onClick = {
                    genderStr = "Other"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(MEDIUM_PADDING)
            )
        }

        Button(
            onClick = {
                if (areDetailsValid(
                        ageStr,
                        weightStr,
                        heightStr,
                        caloriesGoalStr,
                        genderStr,
                        weightGoalStr
                    )
                ) {
                    profileViewModel.saveAllDetails(
                        height = heightStr,
                        age = ageStr,
                        weight = weightStr,
                        gender = genderStr,
                        weightGoal = weightGoalStr,
                        caloriesGoal = caloriesGoalStr
                    )
                    navigateToHomeScreen()
                } else {
                    Toast.makeText(
                        context,
                        "Make Sure All Fields Are Correct And Filled.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryPurple
            )
        ) {
            Text(
                text = "Proceed",
                color = White
            )
        }
    }
}


private fun areDetailsValid(
    vararg args: String
): Boolean {
    for (arg in args) {
        if (arg.isBlank()) {
            return false
        }
    }

    return true
}
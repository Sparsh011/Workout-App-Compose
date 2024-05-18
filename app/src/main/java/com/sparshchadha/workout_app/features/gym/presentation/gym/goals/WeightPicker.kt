package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightPicker(
    weight: Double,
    setWeight: (Double) -> Unit,
    hidePicker: () -> Unit,
    headingText: String,
    setWeightUnit: (String) -> Unit
) {
    var isKGSelected by rememberSaveable {
        mutableStateOf(true)
    }
    BasicAlertDialog(onDismissRequest = { hidePicker() }) {
        Column(
            modifier = Modifier
                .padding(Dimensions.LARGE_PADDING)
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(ColorsUtil.bottomBarColor)
                .padding(Dimensions.LARGE_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = headingText, color = ColorsUtil.primaryTextColor)

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            UnitSelector(
                isKGSelected = isKGSelected,
                setWeightUnit = setWeightUnit,
                setKGRadioButtonSelection = {
                    isKGSelected = it
                }
            )

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            OutlinedTextField(
                value = if (weight.equals(0.0)) "" else weight.toString(),
                onValueChange = {
                    val double = it.toDoubleOrNull()
                    double?.let(setWeight)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = ColorsUtil.bottomBarColor
                ),
                label = {
                    Text(text = "Weight")
                }
            )

            Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

            Button(
                onClick = {
                    hidePicker()
                }
            ) {
                Text(text = "Set Weight", color = Color.White, modifier = Modifier.padding(horizontal = Dimensions.MEDIUM_PADDING))
            }
        }
    }
}

@Composable
private fun UnitSelector(
    setWeightUnit: (String) -> Unit,
    isKGSelected: Boolean,
    setKGRadioButtonSelection: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Use kg")
            RadioButton(
                selected = isKGSelected,
                onClick = {
                    if (!isKGSelected) {
                        setKGRadioButtonSelection(true)
                        setWeightUnit("kg")
                    }
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = ColorsUtil.noAchievementColor
                )
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Use lbs")
            RadioButton(
                selected = !isKGSelected,
                onClick = {
                    if (isKGSelected) {
                        setKGRadioButtonSelection(false)
                        setWeightUnit("lbs")
                    }
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = ColorsUtil.noAchievementColor
                )
            )
        }
    }
}
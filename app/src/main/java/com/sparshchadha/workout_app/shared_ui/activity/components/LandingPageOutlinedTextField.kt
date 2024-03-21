package com.sparshchadha.workout_app.shared_ui.activity.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING

@Composable
fun LandingPageOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showErrorColor: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    isText: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isText) {
                onValueChange(it)
            } else {
                if (it.isDigitsOnly()) onValueChange(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING, vertical = SMALL_PADDING),
        label = {
            Text(text = label, color = primaryTextColor)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = primaryTextColor,
            unfocusedTextColor = primaryTextColor,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = if (showErrorColor) noAchievementColor else primaryBlue,
            unfocusedBorderColor = if (showErrorColor) noAchievementColor else primaryBlue,
            focusedBorderColor = if (showErrorColor) noAchievementColor else primaryBlue
        ),
        keyboardOptions = keyboardOptions
    )
}
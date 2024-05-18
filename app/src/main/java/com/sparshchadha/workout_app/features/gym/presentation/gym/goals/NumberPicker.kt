package com.sparshchadha.workout_app.features.gym.presentation.gym.goals

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun NumberPicker(
    minValue: Int,
    maxValue: Int,
    setCurrentValue: (Int) -> Unit,
    hidePicker: () -> Unit,
    headingText: String,
    showWeightUnits: Boolean = false,
    selectedWeightUnit: (String) -> Unit = {}
) {
    val pickerTextColor = ColorsUtil.primaryTextColor.toArgb()

    BasicAlertDialog(onDismissRequest = { hidePicker() }) {
        Column (
            modifier = Modifier
                .padding(Dimensions.LARGE_PADDING)
                .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                .background(ColorsUtil.bottomBarColor)
                .padding(Dimensions.LARGE_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = headingText, color = ColorsUtil.primaryTextColor)

            Spacer(modifier = Modifier.height(Dimensions.LARGE_PADDING))

            AndroidView(
                factory = {
                    val numberPicker = NumberPicker(it)
                    numberPicker.minValue = minValue
                    numberPicker.maxValue = maxValue
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        numberPicker.textColor = pickerTextColor
                    }
                    numberPicker.setOnValueChangedListener { _, _, newVal ->
                        setCurrentValue(newVal)
                        vibrate(numberPicker)
                    }
                    numberPicker
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimensions.SMALL_PADDING)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun vibrate(view: View) {
    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
}
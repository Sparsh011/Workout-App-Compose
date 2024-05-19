package com.sparshchadha.workout_app.ui.components.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewTimePicker(
    hideTimePicker: () -> Unit,
    updateHour: (Int) -> Unit,
    updateMinutes: (Int) -> Unit,
) {
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }

    val timeDialogState = rememberMaterialDialogState()
    timeDialogState.show()

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                hideTimePicker()
            }
            negativeButton(text = "Cancel") {
                hideTimePicker()
            }
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick a time"
        ) {
            updateMinutes(it.minute)
            updateHour(it.hour)
            pickedTime = it
        }
    }

}
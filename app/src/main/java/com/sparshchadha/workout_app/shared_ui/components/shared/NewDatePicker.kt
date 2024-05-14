package com.sparshchadha.workout_app.shared_ui.components.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewDatePicker(
    hideDatePicker: () -> Unit,
    updateDay: (Int) -> Unit,
    updateMonth: (Int) -> Unit,
    updateYear: (Int) -> Unit,
    showPastDates: Boolean = false
) {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MM yyyy")
                .format(pickedDate)
        }
    }


    val dateDialogState = rememberMaterialDialogState()
    dateDialogState.show()

    MaterialDialog(
        dialogState = dateDialogState,
        onCloseRequest = {
            hideDatePicker()
        },
        buttons = {
            positiveButton(text = "Ok") {
                hideDatePicker()
            }
            negativeButton(text = "Cancel") {
                hideDatePicker()
            }
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
            allowedDateValidator = {
                if (!showPastDates) {
                    it.dayOfMonth >= LocalDate.now().dayOfMonth && it.monthValue >= LocalDate.now().monthValue
                } else {
                    true
                }
            }
        ) {
            updateDay(it.dayOfMonth)
            updateMonth(it.monthValue)
            updateYear(it.year)
            pickedDate = it
        }
    }
}
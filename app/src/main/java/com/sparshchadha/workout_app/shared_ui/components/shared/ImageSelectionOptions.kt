package com.sparshchadha.workout_app.shared_ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSelectionOptions(onDismiss: () -> Unit, selectedOption: (String) -> Unit) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(Dimensions.LARGE_PADDING))
                .padding(LARGE_PADDING)
                .background(ColorsUtil.bottomBarColor),
            horizontalArrangement = Arrangement.Center,
        ){
            Text(
                text = "Camera",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.nonScaledSp,
                color = ColorsUtil.primaryTextColor,
                modifier = Modifier.padding(MEDIUM_PADDING)
                    .clickable {
                        selectedOption("CAMERA")
                    }
            )
            Text(
                text = "Gallery",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.nonScaledSp,
                color = ColorsUtil.primaryTextColor,
                modifier = Modifier.padding(MEDIUM_PADDING)
                    .clickable {
                        selectedOption("GALLERY")
                    }
            )
        }
    }
}
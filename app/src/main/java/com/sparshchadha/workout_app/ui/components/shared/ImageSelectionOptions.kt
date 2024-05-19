package com.sparshchadha.workout_app.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PROFILE_PICTURE_SIZE
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSelectionOptions(onDismiss: () -> Unit, selectedOption: (String) -> Unit) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LARGE_PADDING))
                .height(150.dp)
                .background(ColorsUtil.bottomBarColor),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        selectedOption("CAMERA")
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = null,
                    modifier = Modifier.size(PROFILE_PICTURE_SIZE - LARGE_PADDING),
                    tint = ColorsUtil.carbohydratesColor
                )
                Text(
                    text = "Camera",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.nonScaledSp,
                    color = ColorsUtil.primaryTextColor,
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)
                )
            }

            Column(
                modifier = Modifier
                    .clickable {
                        selectedOption("GALLERY")
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_insert_photo_24),
                    contentDescription = null,
                    modifier = Modifier.size(PROFILE_PICTURE_SIZE - LARGE_PADDING),
                    tint = ColorsUtil.carbohydratesColor
                )
                Text(
                    text = "Gallery",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.nonScaledSp,
                    color = ColorsUtil.primaryTextColor,
                    modifier = Modifier
                        .padding(MEDIUM_PADDING)

                )
            }


        }
    }
}
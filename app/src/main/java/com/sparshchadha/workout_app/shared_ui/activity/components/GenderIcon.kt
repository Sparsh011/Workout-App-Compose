package com.sparshchadha.workout_app.shared_ui.activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING

@Composable
fun GenderIcon(
    modifier: Modifier,
    resourceId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.clickable {
                onClick()
            }
        ) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(SMALL_PADDING))

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = primaryPurple,
                modifier = Modifier.size(
                    LARGE_PADDING
                )
            )
        }
    } else {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = modifier.clickable {
                onClick()
            }
        )
    }
}
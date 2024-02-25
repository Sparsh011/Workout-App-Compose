package com.sparshchadha.workout_app.ui.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PROFILE_PICTURE_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(globalPaddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(SMALL_PADDING)
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) {
        stickyHeader {
            Surface(
                modifier = Modifier.fillParentMaxWidth(),
                color = ColorsUtil.scaffoldBackgroundColor
            ) {
                ProfilePictureAndUserName()
            }
        }

        items(listOf(1,2,3,3,3,3,33,3,3,3,3,3,4,4,4,4,4,4,4,4)) {
            Text(text = it.toString(), fontSize = 20.nonScaledSp)
            CustomDivider()
            Spacer(modifier = Modifier.height(LARGE_PADDING))
        }
    }
}

@Composable
fun ProfilePictureAndUserName() {
    Row(
        modifier = Modifier.fillMaxWidth(),

    ) {
        Box(
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .size(PROFILE_PICTURE_SIZE)
                .weight(3.5f)
                .clip(shape = CircleShape)
                .background(ColorsUtil.primaryBlue)
        ) {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
            text = "Sparsh Chadha",
            fontSize = 24.nonScaledSp,
            modifier = Modifier
                .weight(6.5f)
                .padding(SMALL_PADDING)
        )
    }
}
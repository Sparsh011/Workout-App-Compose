package com.sparshchadha.workout_app.features.profile.presentation.profile

import android.Manifest
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.AlertDialogToUpdate
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PROFILE_PICTURE_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

@Composable
fun ProfilePictureAndUserName(
    name: String,
    onNameChange: (String) -> Unit,
    requestCameraAndStoragePermission: () -> Unit,
    pickImage: () -> Unit,
    imageBitmap: Bitmap?
) {
    var shouldShowNameChangeDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (shouldShowNameChangeDialog) {
        AlertDialogToUpdate(
            hideDialog = {
                shouldShowNameChangeDialog = false
            },
            value = name,
            onConfirmClick = {
                onNameChange(it.trim())
            },
            label = "Your Name",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            isText = true
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = imageBitmap,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(PROFILE_PICTURE_SIZE)
                .clickable {
                    if (!HelperFunctions.hasPermissions(
                            context,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        requestCameraAndStoragePermission()
                    } else {
                        pickImage()
                    }
                }
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    shouldShowNameChangeDialog = true
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name.trim(),
                fontSize = 20.nonScaledSp,
                modifier = Modifier
                    .padding(SMALL_PADDING),
                color = primaryTextColor,
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(SMALL_PADDING))

            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = primaryTextColor,
                modifier = Modifier.size(LARGE_PADDING)
            )
        }
    }
}
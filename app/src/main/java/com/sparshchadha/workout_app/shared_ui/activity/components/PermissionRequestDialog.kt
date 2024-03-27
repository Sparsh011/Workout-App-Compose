package com.sparshchadha.workout_app.shared_ui.activity.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun PermissionRequestDialog(
    description: String,
    hideDialog: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
            hideDialog()
        },
        confirmButton = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        },
        title = {
            DialogText(
                text = "Need Permission",
                fontSize = 20.nonScaledSp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            DialogText(
                text = description,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Normal
            )
        }
    )
}

@Composable
fun DialogText(
    text: String,
    fontSize: TextUnit,
    fontWeight: FontWeight
) {
    Text(
        text = text,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight,
        color = primaryTextColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMALL_PADDING)
    )
}
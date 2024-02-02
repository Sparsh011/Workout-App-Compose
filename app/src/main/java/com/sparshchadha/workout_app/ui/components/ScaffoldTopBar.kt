package com.sparshchadha.workout_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun ScaffoldTopBar(topBarDescription: String, onBackButtonPressed: () -> Unit = {}, showBackIcon: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    ) {
        if (showBackIcon) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onBackButtonPressed()
                    }
            )
        }

        Text(
            text = topBarDescription,
            color = Color.Black,
            fontSize = 22.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}
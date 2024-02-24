package com.sparshchadha.workout_app.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import java.util.Locale

object Extensions {
    fun String.capitalize(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

    val Int.nonScaledSp
        @Composable
        get() = (this / LocalDensity.current.fontScale).sp
}
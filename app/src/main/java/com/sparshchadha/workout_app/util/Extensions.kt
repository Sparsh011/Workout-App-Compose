package com.sparshchadha.workout_app.util

import java.util.Locale

object Extensions {
    fun String.capitalize() : String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
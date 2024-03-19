package com.sparshchadha.workout_app.storage.shared_preference

import android.content.Context

class WorkoutAppSharedPref(
    context: Context
) {

    private val sharedPreferences = context.getSharedPreferences("WorkoutAppSharedPref", Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("isDarkTheme", false)
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }
}

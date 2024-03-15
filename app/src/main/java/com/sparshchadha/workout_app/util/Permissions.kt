package com.sparshchadha.workout_app.util

import android.Manifest

object Permissions {
    fun getCameraAndStoragePermissions(): List<Pair<String, Int>> {
        return listOf(
            Pair(Manifest.permission.CAMERA, -1),
            Pair(Manifest.permission.READ_MEDIA_IMAGES, 33),
            Pair(Manifest.permission.READ_EXTERNAL_STORAGE, -1),
        )
    }
}
package com.sparshchadha.workout_app.ui.activity

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    permissionState: PermissionState,
    showCameraPreviewScreen: () -> Unit
) {
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {

        },
        permissionNotAvailableContent = { /* ... */ }
    ) {
        showCameraPreviewScreen()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskNotificationPermission(
    permissionState: PermissionState,
    onNotificationPermissionGranted: () -> Unit
) {
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
        },
        permissionNotAvailableContent = {
            // If notification permission not present, no need of permission
        }
    ) {
        onNotificationPermissionGranted()
    }
}
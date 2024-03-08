package com.sparshchadha.workout_app.ui.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.activity.components.LandingPage
import com.sparshchadha.workout_app.ui.activity.components.PermissionRequestDialog
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.nav_graph.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.viewmodel.FoodItemsViewModel
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.viewmodel.RemindersViewModel
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivityTaggg"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val foodItemsViewModel: FoodItemsViewModel by viewModels()
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val remindersViewModel: RemindersViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WorkoutAppTheme {
                requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {

                        } else {

                        }
                    }

                when (profileViewModel.darkTheme.collectAsStateWithLifecycle().value) {
                    true -> {

                    }
                    false -> {

                    }
                }

                val pickMedia =
                    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                        if (uri != null) {
                            val bitmap = getCapturedImage(uri)
                            profileViewModel.setImageBitmap(bitmap)
                            profileViewModel.cacheBitmap(bitmap)
                        } else {
                            Log.d(TAG, "No media selected")
                        }
                        profileViewModel.galleryClosed()
                    }

                val openGallery by profileViewModel.openGallery.collectAsStateWithLifecycle()

                if (openGallery == true) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                val showPermissionDialogFlow by profileViewModel.showPermissionDialog.collectAsStateWithLifecycle()
                val requestPermissions by profileViewModel.requestPermissions.collectAsStateWithLifecycle()

                when (showPermissionDialogFlow) {
                    true -> {
                        PermissionRequestDialog(description = "Need Permission") {
                            profileViewModel.hidePermissionDialog()
                        }
                    }

                    false -> {

                    }

                    else -> {
                        // ignore
                    }
                }

                if (requestPermissions != null) {
                    if (requestPermissions?.size == 1) {
                        LaunchedEffect(key1 = Unit) {
                            requestPermissionHandler(permission = requestPermissions!![0])
                        }

                    } else {
                        LaunchedEffect(key1 = Unit) {
                            requestMultiplePermissions(permissions = requestPermissions!!.toTypedArray())
                        }
                    }
                }

                val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
                var navigateToHomeScreen by remember {
                    mutableStateOf(false)
                }

                if (sharedPreferences.getBoolean(
                        "landing_page_shown",
                        false
                    ) || navigateToHomeScreen
                ) {
                    val navHostController = rememberNavController()
                    val gymExercises = workoutViewModel.gymExercisesFromApi.value
                    val yogaPoses = workoutViewModel.yogaPosesFromApi.value

                    if (sharedPreferences.getBoolean("app_first_launch", true)) {
                        LaunchedEffect(key1 = Unit) {
                            requestPermission(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        setupFirstAppLaunch()
                    }

                    Scaffold(
                        bottomBar = {
                            BottomBar(navHostController = navHostController)
                        },
                        containerColor = scaffoldBackgroundColor
                    ) {
                        NavGraph(
                            navController = navHostController,
                            globalPaddingValues = it,
                            foodItemsViewModel = foodItemsViewModel,
                            workoutViewModel = workoutViewModel,
                            gymExercises = gymExercises,
                            yogaPoses = yogaPoses,
                            remindersViewModel = remindersViewModel,
                            profileViewModel = profileViewModel
                        )
                    }
                } else {
                    LandingPage(
                        profileViewModel,
                        navigateToHomeScreen = {
                            landingPageShown {
                                navigateToHomeScreen = true
                            }
                        }
                    )
                }
            }
        }
    }

    private fun setupFirstAppLaunch() {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("app_first_launch", false)
        editor.apply()
    }

    private fun landingPageShown(
        navigateToHomeScreen: () -> Unit
    ) {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("landing_page_shown", true)
        editor.apply()
        navigateToHomeScreen()
    }

    private fun requestPermissionHandler(
        permission: String,
        forAndroidVersion: Int = -1,
        fallbackPermission: String = ""
    ) {
        if (forAndroidVersion != -1 && fallbackPermission.isNotBlank()) {
            if (Build.VERSION.SDK_INT > forAndroidVersion) {
                requestPermission(permission = permission)
            } else {
                requestPermission(fallbackPermission)
            }
        } else {
            requestPermission(permission = permission)
        }
    }

    private fun requestPermission(permission: String) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) -> {

            }

            else -> {
                requestPermissionLauncher.launch(permission)
                ActivityCompat.requestPermissions(this, arrayOf(permission), 123)
            }
        }
    }

    private fun requestMultiplePermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, 123)

    }

    @Composable
    fun LaunchCamera() {

    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )

            else -> {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    fun Activity.getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
    }
}

package com.sparshchadha.workout_app.shared_ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.features.reminders.presentation.viewmodels.RemindersViewModel
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.shared_ui.activity.components.GoogleSignInLauncher
import com.sparshchadha.workout_app.shared_ui.activity.components.LandingPage
import com.sparshchadha.workout_app.shared_ui.activity.components.PermissionRequestDialog
import com.sparshchadha.workout_app.shared_ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.shared_ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.shared_ui.navigation.nav_graph.NavGraph
import com.sparshchadha.workout_app.shared_ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


private const val TAG = "MainActivityTaggg"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val foodItemsViewModel: FoodAndWaterViewModel by viewModels()
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val remindersViewModel: RemindersViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()
    private val yogaViewModel: YogaViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var analytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {

            when(profileViewModel.darkTheme.collectAsStateWithLifecycle().value) {
                null -> {
                    // show splash screen
                }
                else -> {
                    WorkoutAppTheme (
                        darkTheme = profileViewModel.darkTheme.collectAsState().value!!
                    ){
                        analytics = Firebase.analytics
                        GoogleSignInLauncher(profileViewModel = profileViewModel)

                        requestPermissionLauncher =
                            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                                if (isGranted) {

                                } else {

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
                        val context = LocalContext.current

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
                                    val permission = requestPermissions!![0].first
                                    val minSdk = requestPermissions!![0].second
                                    if (minSdk != -1) {
                                        requestPermissionHandler(permission = permission, minSdk = minSdk)
                                    } else {
                                        requestPermissionHandler(permission = permission)
                                    }
                                }

                            } else {
                                LaunchedEffect(key1 = Unit) {
                                    val permissions = mutableListOf<String>()
                                    requestPermissions?.forEach {
                                        permissions.add(it.first)
                                    }
                                    requestMultiplePermissions(permissions = permissions.toTypedArray())
                                }
                            }
                        }

                        when(profileViewModel.isFirstTimeAppOpen.collectAsState().value) {
                            "true" -> {
                                LandingPage(
                                    profileViewModel,
                                    navigateToHomeScreen = {
                                        profileViewModel.updateFirstTimeAppOpen("false")
                                    }
                                )
                            }
                            "false" -> {
                                val navHostController = rememberNavController()

                                if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    LaunchedEffect(key1 = Unit) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            requestPermission(Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    }
                                    profileViewModel.updateFirstTimeAppOpen("false")
                                }

                                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                                Scaffold(
                                    bottomBar = {
                                        BottomBar(
                                            navHostController = navHostController,
                                            bottomBarState = bottomBarState.value
                                        )
                                    },
                                    containerColor = scaffoldBackgroundColor
                                ) {
                                    NavGraph(
                                        navController = navHostController,
                                        globalPaddingValues = it,
                                        foodItemsViewModel = foodItemsViewModel,
                                        workoutViewModel = workoutViewModel,
                                        remindersViewModel = remindersViewModel,
                                        profileViewModel = profileViewModel,
                                        newsViewModel = newsViewModel,
                                        yogaViewModel = yogaViewModel,
                                        toggleBottomBarVisibility = { bottomBarVisibility ->
                                            bottomBarState.value = bottomBarVisibility
                                        }
                                    )
                                }
                            }
                            else -> {
                                ShowLoadingScreen()
                                val loadingTimeElapsed = remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(2000)
                                    loadingTimeElapsed.value = true
                                }
                                if (loadingTimeElapsed.value) {
                                    profileViewModel.updateFirstTimeAppOpen("true")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requestPermissionHandler(
        permission: String,
        minSdk: Int = -1,
        fallbackPermission: String = ""
    ) {
        if (minSdk != -1 && fallbackPermission.isNotBlank()) {
            if (Build.VERSION.SDK_INT > minSdk) {
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
}

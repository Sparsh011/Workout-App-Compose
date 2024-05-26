package com.sparshchadha.workout_app.ui.activity

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.Network
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.camerax.CameraPreviewScreen
import com.sparshchadha.workout_app.features.food.presentation.viewmodels.FoodAndWaterViewModel
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.features.news.presentation.viewmodels.NewsViewModel
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.features.reminders.presentation.viewmodels.RemindersViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.ImageSelectors
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.features.yoga.presentation.viewmodels.YogaViewModel
import com.sparshchadha.workout_app.ui.activity.components.GoogleSignInLauncher
import com.sparshchadha.workout_app.ui.activity.components.LandingPage
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.components.ui_state.ShowLoadingScreen
import com.sparshchadha.workout_app.ui.navigation.nav_graph.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.util.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID


private const val TAG = "MainActivityTaggg"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val foodItemsViewModel: FoodAndWaterViewModel by viewModels()
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val remindersViewModel: RemindersViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()
    private val yogaViewModel: YogaViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            observeInternetConnection()
            when (val darkTheme = profileViewModel.darkTheme.collectAsStateWithLifecycle().value) {
                null -> {}

                else -> {
                    WorkoutAppTheme(
                        darkTheme = darkTheme
                    ) {
                        setupAnalytics()
                        GoogleSignInLauncher(sharedViewModel = sharedViewModel)
                        val imageSelector = sharedViewModel.mImageSelector.collectAsState().value
                        SetupPhotoPickerLauncher(imageSelector)

                        val openGallery by sharedViewModel.openGallery.collectAsStateWithLifecycle()
                        val openCamera by sharedViewModel.openCamera.collectAsStateWithLifecycle()

                        if (openGallery == true) {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        val context = LocalContext.current


                        if (openCamera == true) {
                            if (!HelperFunctions.hasPermissions(this, Manifest.permission.CAMERA)) {
                                val cameraPermissionState =
                                    rememberPermissionState(permission = Manifest.permission.CAMERA)
                                CameraPermission(
                                    permissionState = cameraPermissionState,
                                    showCameraPreviewScreen = {

                                    }
                                )
                                LaunchedEffect(key1 = Unit) {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            } else {
                                CameraPreviewScreen(
                                    hideCameraPreviewScreen = {
                                        sharedViewModel.dismissCamera()
                                    },
                                    onImageCaptured = {
                                        sharedViewModel.dismissCamera()
                                        if (it != null) handleImageSelection(it, ImageSelectors.PROFILE_PIC)
                                    }
                                )
                            }
                        } else {
                            when (sharedViewModel.isFirstTimeAppOpen.collectAsState().value) {
                                "true" -> {
                                    LandingPage(
                                        profileViewModel,
                                        navigateToHomeScreen = {
                                            profileViewModel.updateFirstTimeAppOpen("false")
                                        }
                                    )
                                }

                                "false" -> {
                                    SetupLauncherScreen(context)
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
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun SetupLauncherScreen(
        context: Context,
    ) {
        val navHostController = rememberNavController()
        var askNotifPermission by rememberSaveable {
            mutableStateOf(false)
        }

        if (askNotifPermission) {
            val notificationPermissionState =
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            AskNotificationPermission(
                permissionState = notificationPermissionState,
                onNotificationPermissionGranted = {}
            )
            LaunchedEffect(key1 = Unit) {
                notificationPermissionState.launchPermissionRequest()
            }
        }

        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            LaunchedEffect(key1 = Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    askNotifPermission = true
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
            containerColor = ColorsUtil.scaffoldBackgroundColor
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
                },
                sharedViewModel = sharedViewModel
            )
        }
    }

    @Composable
    private fun SetupPhotoPickerLauncher(
        imageSelector: ImageSelectors
    ) {
        pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    handleImageSelection(uri, imageSelector)
                } else {
                    Log.d(TAG, "No media selected")
                }
                sharedViewModel.dismissGallery()
            }
    }

    private fun setupAnalytics() {
        analytics = Firebase.analytics
    }

    private fun observeInternetConnection() {
        if (NetworkManager.checkInternet(this)) {
            sharedViewModel.updateInternetStatus(true)
        } else {
            sharedViewModel.updateInternetStatus(false)
        }
        val networkCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    sharedViewModel.updateInternetStatus(true)
                }

                override fun onLost(network: Network) {
                    sharedViewModel.updateInternetStatus(false)
                }
            }

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun handleImageSelection(uri: Uri, imageSelector: ImageSelectors) {
        val bitmap = getCapturedImageBitmap(uri)
        when (imageSelector) {
            ImageSelectors.NONE -> {

            }

            ImageSelectors.PROFILE_PIC -> {
                profileViewModel.setImageBitmap(bitmap)
                profileViewModel.cacheBitmap(bitmap)
            }

            ImageSelectors.DISH -> {
                val imagePath = saveImageToInternalStorage(bitmap)
                sharedViewModel.setSelectedImageUri(uri)
                sharedViewModel.setImagePath(imagePath)
            }

            ImageSelectors.BODY_PROGRESS -> {

            }
        }
    }

    private fun getCapturedImageBitmap(selectedPhotoUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )

            else -> {
                try {
                    val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                    ImageDecoder.decodeBitmap(source)
                } catch (e: Exception) {
                    Log.e(TAG, "getCapturedImageBitmap: ${e.localizedMessage}")
                    resources.getDrawable(R.drawable.baseline_image_24).toBitmap()
                }
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper =
            ContextWrapper(applicationContext) // This wrapper will tell which application wants to save image to gallery.

        var file = wrapper.getDir(
            "NutriWorkoutCompanion",
            Context.MODE_PRIVATE
        ) //the default mode, where the created file can only be accessed by the calling application (or all applications sharing the same user ID).

        file = File(file, "${UUID.randomUUID()}.jpg")

//        creating bitmap of the image -
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
//            Note - early all stream instances do not actually need to be closed after use. Generally, only streams whose source is an IO channel (such as those returned by Files.lines(Path, Charset)) will require closing. Closing the stream for resource management.
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath // Returns the directory where the file exists as well as the name of the file.
    }
}

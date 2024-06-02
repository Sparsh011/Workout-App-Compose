package com.sparshchadha.workout_app.features.camerax

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.gym.presentation.gym.goals.vibrate
import com.sparshchadha.workout_app.util.Dimensions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreviewScreen(
    hideCameraPreviewScreen: () -> Unit,
    onImageCaptured: (Uri?) -> Unit
) {
//    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }
    var cameraSelector: CameraSelector
    val imageCapture = remember { ImageCapture.Builder().build() }
    var cameraProvider: ProcessCameraProvider? = null

    LaunchedEffect(lensFacing) {
        cameraProvider = context.getCameraProvider()
        cameraProvider!!.unbindAll()
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        cameraProvider!!.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = hideCameraPreviewScreen) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            hideCameraPreviewScreen()
                            cameraProvider!!.unbindAll()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_flip_camera_android_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(Dimensions.LARGE_PADDING)
                        .size(Dimensions.LARGE_PADDING + Dimensions.LARGE_PADDING)
                        .clickable {
                            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                CameraSelector.LENS_FACING_FRONT
                            } else {
                                CameraSelector.LENS_FACING_BACK
                            }
                            vibrate(previewView)
                        },
                    tint = Color.White
                )

                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(Dimensions.LARGE_PADDING)
                        .size(Dimensions.LARGE_PADDING + Dimensions.LARGE_PADDING)
                        .clickable {
                            captureImage(
                                imageCapture,
                                context,
                                onImageCaptured = {
                                    onImageCaptured(it)
                                    cameraProvider!!.unbindAll()
                                }
                            )
                            vibrate(previewView)
                        },
                    tint = Color.White
                )
            }
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onImageCaptured: (Uri?) -> Unit
) {
    val name = "CameraxImage.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/NutriWorkoutCompanion")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageCaptured(outputFileResults.savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                println("Failed $exception")
            }
        }
    )
}
package com.sparshchadha.workout_app.features.shared.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.storage.datastore.WorkoutAppDatastorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SharedViewModelTaggg"

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val datastorePreference: WorkoutAppDatastorePreference
) : ViewModel() {
    private val _showPermissionDialog = MutableStateFlow<Boolean?>(null)
    val showPermissionDialog = _showPermissionDialog.asStateFlow()

    private val _requestPermissions = MutableStateFlow<List<Pair<String, Int>>?>(null)
    val requestPermissions = _requestPermissions.asStateFlow()

    private val _openCamera = MutableStateFlow<Boolean?>(null)
    val openCamera = _openCamera.asStateFlow()

    private val _openGallery = MutableStateFlow<Boolean?>(null)
    val openGallery = _openGallery.asStateFlow()

    private val _startGoogleSignIn = MutableStateFlow(false)
    val startGoogleSignIn = _startGoogleSignIn.asStateFlow()

    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult = _loginResult.asStateFlow()

    private val _loginToken = MutableStateFlow("")
    val loginToken = _loginToken.asStateFlow()

    private val _isFirstTimeAppOpen = MutableStateFlow<String?>(null)
    val isFirstTimeAppOpen = _isFirstTimeAppOpen.asStateFlow()

    private val _imageSelector = MutableStateFlow(ImageSelectors.NONE)
    val mImageSelector = _imageSelector.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _selectedImagePath = MutableStateFlow<String?>(null)
    val selectedImagePath = _selectedImagePath.asStateFlow()

    init {
        readLoginToken()
        readFirstTimeAppOpen()
    }

    private fun readFirstTimeAppOpen() {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.readFirstAppOpen.collect {
                _isFirstTimeAppOpen.value = it
            }
        }
    }

    private fun readLoginToken() {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.readLoginToken.collect {
                it?.let {
                    _loginToken.value = it
                }
            }
        }
    }

    fun showPermissionDialog() {
        _showPermissionDialog.value = true
    }

    fun hidePermissionDialog() {
        _showPermissionDialog.value = false
    }

    fun requestPermissions(permissions: List<Pair<String, Int>>) {
        _requestPermissions.value = permissions
    }

    fun openGallery(imageSelector: ImageSelectors) {
        _openGallery.value = true
        _imageSelector.value = imageSelector
    }

    fun galleryClosed() {
        _openGallery.value = false
    }

    fun openCamera() {
        _openCamera.value = true
    }

    fun cameraClosed() {
        _openCamera.value = false
    }

    fun startGoogleSignIn() {
        _startGoogleSignIn.value = true
    }

    fun updateLoginResult(result: Boolean, token: String = "") {
        _loginResult.value = result
        Log.e(TAG, "updateLoginResult: $token")
        if (token.isNotBlank()) {
            // send token to server and save token in datastore
            viewModelScope.launch(Dispatchers.IO) {
                datastorePreference.saveLoginToken(token)
            }
        }
        _startGoogleSignIn.value = false
    }

    fun setSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun setImagePath(imagePath: String) {
        _selectedImagePath.value = imagePath
    }
}

enum class ImageSelectors {
    NONE,
    PROFILE_PIC,
    DISH,
    BODY_PROGRESS
}

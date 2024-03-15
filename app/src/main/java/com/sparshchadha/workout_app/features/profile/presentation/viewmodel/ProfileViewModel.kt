package com.sparshchadha.workout_app.features.profile.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.LruCache
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparshchadha.workout_app.storage.datastore.WorkoutAppDatastorePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val datastorePreference: WorkoutAppDatastorePreference,
) : ViewModel() {
    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _height = MutableStateFlow("")
    val height: StateFlow<String> = _height

    private val _currentWeight = MutableStateFlow("")
    val currentWeight: StateFlow<String> = _currentWeight

    private val _weightGoal = MutableStateFlow("")
    val weightGoal: StateFlow<String> = _weightGoal

    private val _caloriesGoal = MutableStateFlow("")
    val caloriesGoal: StateFlow<String> = _caloriesGoal

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _profilePicBitmap = MutableStateFlow<Bitmap?>(null)
    val profilePicBitmap = _profilePicBitmap

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme

    private val _waterGlassesGoal = MutableStateFlow(0)
    val waterGlassesGoal: StateFlow<Int> = _waterGlassesGoal

    private lateinit var bitmapLruCache: LruCache<String, Bitmap>

    init {
        readAge()
        readGender()
        readHeight()
        readCurrentWeight()
        readWeightGoal()
        readCaloriesGoal()
        readName()
        readBase64ProfilePic()
        setupLruCache()
        readDarkTheme()
        readWaterGlassesGoal()
    }

    private fun readWaterGlassesGoal() {
        viewModelScope.launch {
            datastorePreference.readWaterGlassesGoal.collect { goal ->
                _waterGlassesGoal.value = goal?.toInt() ?: 8
            }
        }
    }

    private fun readDarkTheme() {
        viewModelScope.launch {
            datastorePreference.readDarkMode.collect { isDarkTheme ->
                _darkTheme.value = isDarkTheme.toBoolean()
            }
        }
    }

    private fun setupLruCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        bitmapLruCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    private fun readAge() {
        viewModelScope.launch {
            datastorePreference.readAge.collect { age ->
                _age.value = age ?: ""
            }
        }
    }

    private fun readGender() {
        viewModelScope.launch {
            datastorePreference.readGender.collect { gender ->
                _gender.value = gender ?: ""
            }
        }
    }

    private fun readHeight() {
        viewModelScope.launch {
            datastorePreference.readHeight.collect { height ->
                _height.value = height ?: ""
            }
        }
    }

    private fun readCurrentWeight() {
        viewModelScope.launch {
            datastorePreference.readCurrentWeight.collect { currentWeight ->
                _currentWeight.value = currentWeight ?: ""
            }
        }
    }

    private fun readWeightGoal() {
        viewModelScope.launch {
            datastorePreference.readWeightGoal.collect { weightGoal ->
                _weightGoal.value = weightGoal ?: ""
            }
        }
    }

    private fun readCaloriesGoal() {
        viewModelScope.launch {
            datastorePreference.readCaloriesGoal.collect { caloriesGoal ->
                _caloriesGoal.value = caloriesGoal ?: ""
            }
        }
    }

    private fun readName() {
        viewModelScope.launch {
            datastorePreference.readName.collect { name ->
                _name.value = name ?: "Guest"
            }
        }
    }

    private fun readBase64ProfilePic() {
        try {
            if (bitmapLruCache.get("profileKey") != null) {
                _profilePicBitmap.value = bitmapLruCache.get("profileKey")
            }
        } catch (_: Exception) {

        }
        viewModelScope.launch {
            datastorePreference.readBase64ProfilePic.collect { profilePic ->
                _profilePicBitmap.value = DbBitmapUtility.decodeBase64(profilePic ?: "")
            }
        }
    }

    private val _showPermissionDialog = MutableStateFlow<Boolean?>(null)
    val showPermissionDialog = _showPermissionDialog.asStateFlow()

    private val _requestPermissions = MutableStateFlow<List<String>?>(null)
    val requestPermissions = _requestPermissions.asStateFlow()

    private val _openCamera = MutableStateFlow<Boolean?>(null)
    val openCamera = _openCamera.asStateFlow()

    private val _openGallery = MutableStateFlow<Boolean?>(null)
    val openGallery = _openGallery.asStateFlow()

    private val _profileImageUri = MutableStateFlow<Bitmap?>(null)

    fun saveAge(age: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveAge(age)
        }
    }

    fun saveGender(gender: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveGender(gender)
        }
    }

    fun saveHeight(height: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveHeight(height)
        }
    }

    fun saveCurrentWeight(weight: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveCurrentWeight(weight)
        }
    }

    fun saveCaloriesGoal(newCaloriesGoal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveCaloriesGoal(newCaloriesGoal)
        }
    }

    fun saveWeightGoal(newWeightGoal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveWeightGoal(newWeightGoal)
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveName(name)
        }
    }

    fun saveAllDetails(
        age: String,
        height: String,
        weight: String,
        gender: String,
        weightGoal: String,
        caloriesGoal: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            async {
                datastorePreference.saveAge(age)
            }
            async {
                datastorePreference.saveHeight(height)
            }
            async {
                datastorePreference.saveCurrentWeight(weight)
            }
            async {
                datastorePreference.saveGender(gender)
            }

            async {
                datastorePreference.saveWeightGoal(weightGoal)
            }

            async {
                datastorePreference.saveCaloriesGoal(caloriesGoal)
            }
        }
    }

    fun showPermissionDialog() {
        _showPermissionDialog.value = true
    }

    fun hidePermissionDialog() {
        _showPermissionDialog.value = false
    }

    fun requestPermissions(permissions: List<String>) {
        _requestPermissions.value = permissions
    }

    fun openGallery() {
        _openGallery.value = true
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

    fun setImageBitmap(imgBitmap: Bitmap) {
        _profileImageUri.value = imgBitmap
        val base64Image = DbBitmapUtility.encodeToBase64(imgBitmap, Bitmap.CompressFormat.JPEG, 50)

        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveBase64Image(imageStr = base64Image)
        }
    }

    fun getBase64Image(base64Image: String): Bitmap? {
        return DbBitmapUtility.decodeBase64(base64Image)
    }

    fun cacheBitmap(bitmap: Bitmap) {
        bitmapLruCache.put("profileKey", bitmap)
    }

    fun enableDarkMode(setTo: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.setDarkMode(setTo.toString())
        }
    }

    fun setWaterGlassesGoal(goal: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            datastorePreference.saveWaterGlassesGoal(goal.toString())
        }
    }
}

private object DbBitmapUtility {
    fun encodeToBase64(image: Bitmap, compressFormat: Bitmap.CompressFormat?, quality: Int): String? {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat!!, quality, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
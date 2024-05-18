package com.sparshchadha.workout_app.features.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.DialogToUpdateValue
import com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.food.FoodSettingsCategory
import com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.gym.GymSettingsCategories
import com.sparshchadha.workout_app.features.profile.presentation.profile.settings_categories.yoga.YogaSettingsCategory
import com.sparshchadha.workout_app.features.profile.presentation.profile.shared.SettingsCategoryHeader
import com.sparshchadha.workout_app.features.profile.presentation.viewmodel.ProfileViewModel
import com.sparshchadha.workout_app.features.shared.viewmodels.ImageSelectors
import com.sparshchadha.workout_app.features.shared.viewmodels.SharedViewModel
import com.sparshchadha.workout_app.shared_ui.components.shared.ImageSelectionOptions
import com.sparshchadha.workout_app.shared_ui.screens.workout.HeaderText
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

private const val TAG = "ProfileScreenTagggg"

@Composable
fun ProfileScreen(
    globalPaddingValues: PaddingValues,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    sharedViewModel: SharedViewModel
) {
    val height = profileViewModel.height.collectAsState().value
    val weightGoal = profileViewModel.weightGoal.collectAsState().value
    val currentWeight = profileViewModel.currentWeight.collectAsState().value
    val gender = profileViewModel.gender.collectAsState().value
    val age = profileViewModel.age.collectAsState().value
    val caloriesGoal = profileViewModel.caloriesGoal.collectAsState().value
    val name = profileViewModel.name.collectAsState().value
    val profileBitmap = profileViewModel.profilePicBitmap.collectAsState().value
    val loginToken = sharedViewModel.loginToken.collectAsState().value

    var shouldShowDialogToUpdateValue by remember {
        mutableStateOf(false)
    }

    var currentValue by remember {
        mutableStateOf("")
    }

    var shouldShowImageSelectionOptions by remember {
        mutableStateOf(false)
    }

    if (shouldShowDialogToUpdateValue) {
        DialogToUpdateValue(
            updateValueOf = currentValue,
            profileViewModel = profileViewModel,
            onDismiss = {
                shouldShowDialogToUpdateValue = false
            },
            height = height,
            weightGoal = weightGoal,
            currentWeight = currentWeight,
            age = age,
            gender = gender,
            caloriesGoal = caloriesGoal
        )
    }

    if (shouldShowImageSelectionOptions) {
        ImageSelectionOptions(
            onDismiss = {
                shouldShowImageSelectionOptions = false
            },
            selectedOption = { it ->
                when (it) {
                    "CAMERA" -> {
                        sharedViewModel.openCamera()
                    }
                    "GALLERY" -> {
                        sharedViewModel.openGallery(imageSelector = ImageSelectors.PROFILE_PIC)
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
            .background(scaffoldBackgroundColor)
    ) {
        Text(
            text = "App Settings",
            fontSize = 20.nonScaledSp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorsUtil.statusBarColor)
                .padding(
                    start = MEDIUM_PADDING,
                    end = SMALL_PADDING,
                    top = SMALL_PADDING,
                    bottom = MEDIUM_PADDING
                ),
        )

        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            ProfilePictureAndUserName(
                name = name,
                onNameChange = { newName ->
                    profileViewModel.saveName(newName)
                },
                imageBitmap = profileBitmap,
                showImageSelectionOptions = {
                    shouldShowImageSelectionOptions = true
                }
            )

            HeaderText(heading = "Settings")

            AppSettings(
                setDarkTheme = {
                    profileViewModel.enableDarkMode(it)
                },
                isDarkTheme = profileViewModel.darkTheme.collectAsState().value,
                onAuthButtonClick = {
                    if (loginToken.isBlank()) {
                        sharedViewModel.startGoogleSignIn()
                    } else {
                        profileViewModel.signOutUser()
                        navController.popBackStack()
                    }
                },
                loginToken = loginToken
            )

            HeaderText(heading = "Personal Details")

            PersonalInformation(
                height = height,
                weight = currentWeight,
                bmi = getBmi(height, currentWeight),
                gender = gender,
                age = age,
                weightGoal = weightGoal,
                caloriesGoal = caloriesGoal,
                onItemClick = { item ->
                    HelperFunctions.getPersonalInfoCategories().forEach {
                        if (item == it) {
                            shouldShowDialogToUpdateValue = true
                            currentValue = item
                        }
                    }
                }
            )

            SettingsCategoryHeader(text = "Gym Workouts")

            GymSettingsCategories(
                navigateToRoute = {
                    navController.navigate(it)
                }
            )

            SettingsCategoryHeader(text = "Yoga")

            YogaSettingsCategory(
                navigateToRoute = {
                    navController.navigate(it)
                }
            )

            SettingsCategoryHeader(text = "Calories & Food")

            FoodSettingsCategory(
                navigateToRoute = {
                    navController.navigate(it)
                }
            )
        }
    }
}

fun getBmi(height: String, currentWeight: String): String {
    if (height.isNotBlank() && currentWeight.isNotBlank()) {
        val bmi = (currentWeight.toDouble() * 10_000) / (height.toDouble() * height.toDouble())
        return String.format("%.2f", bmi)
    }

    return "0"
}
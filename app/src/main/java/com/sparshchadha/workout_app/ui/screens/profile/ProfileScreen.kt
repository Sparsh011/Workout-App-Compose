package com.sparshchadha.workout_app.ui.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.GenderIcon
import com.sparshchadha.workout_app.LandingPageOutlinedTextField
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.ui.components.shared.CustomDivider
import com.sparshchadha.workout_app.ui.screens.workout.HeaderText
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.bottomBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.ColorsUtil.targetAchievedColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.PROFILE_PICTURE_SIZE
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import com.sparshchadha.workout_app.viewmodel.ProfileViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    globalPaddingValues: PaddingValues,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val height =
        profileViewModel.readHeight.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val weightGoal =
        profileViewModel.readWeightGoal.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val currentWeight =
        profileViewModel.readCurrentWeight.collectAsStateWithLifecycle(initialValue = "").value
            ?: ""
    val gender =
        profileViewModel.readGender.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val age = profileViewModel.readAge.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val caloriesGoal =
        profileViewModel.readCaloriesGoal.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val name =
        profileViewModel.readName.collectAsStateWithLifecycle(initialValue = "").value ?: "Guest"

    var shouldShowDialogToUpdateValue by remember {
        mutableStateOf(false)
    }

    var showDialogToUpdateValueOf by remember {
        mutableStateOf("")
    }

    if (shouldShowDialogToUpdateValue) {
        DialogToUpdate(
            showDialogToUpdateValueOf = showDialogToUpdateValueOf,
            profileViewModel = profileViewModel,
            hideDialog = {
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


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
            .background(scaffoldBackgroundColor)
    ) {
        stickyHeader {
            StickyHeaderTextContent(
                modifier = Modifier
                    .fillParentMaxWidth(),
                text = "Your Profile"
            )
        }

        item {
            ProfilePictureAndUserName(
                name = name,
                onNameChange = { newName ->
                    profileViewModel.saveName(newName)
                }
            )
        }

        item {
            HeaderText(heading = "Personal Details")
        }

        item {
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
                            showDialogToUpdateValueOf = item
                        }
                    }
                }
            )
        }

        item {
            SettingsCategoryHeader(text = "Gym Workouts")
        }

        items(HelperFunctions.settingsForGym().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForGym()[it],
                onClick = {
                    handleGymItemClick(
                        clickedItem = HelperFunctions.settingsForGym()[it],
                        navigateToScreen = { route ->
                            navController.navigate(route)
                        }
                    )
                },
                verticalLineColor = primaryBlue,
                showDivider = it != HelperFunctions.settingsForGym().size - 1
            )
        }

        item {
            SettingsCategoryHeader(text = "Yoga")
        }

        items(HelperFunctions.settingsForYoga().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForYoga()[it],
                onClick = {
                    handleYogaItemClick(
                        clickedItem = HelperFunctions.settingsForYoga()[it],
                        navigateToScreen = { route ->
                            navController.navigate(route)
                        }
                    )
                },
                verticalLineColor = primaryBlue,
                showDivider = it != HelperFunctions.settingsForYoga().size - 1
            )
        }

        item {
            SettingsCategoryHeader(text = "Calories & Food")
        }

        items(HelperFunctions.settingsForCalorieTracker().size) {
            SettingsCategory(
                text = HelperFunctions.settingsForCalorieTracker()[it],
                onClick = {
                    handleCaloriesTrackerItemClick(
                        clickedItem = HelperFunctions.settingsForCalorieTracker()[it],
                        navigateToScreen = { route ->
                            navController.navigate(route)
                        }
                    )
                },
                verticalLineColor = primaryBlue,
                showDivider = it != HelperFunctions.settingsForCalorieTracker().size - 1
            )
        }
    }
}

fun handleCaloriesTrackerItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Food" -> {

        }

        "Activity" -> {

        }

        "Goals" -> {

        }

        "Saved Food Items" -> {
            navigateToScreen("SavedItemsScreen/calorieTracker")
        }
    }
}

fun handleGymItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Workouts" -> {

        }

        "Activity" -> {

        }

        "Personal Records" -> {

        }

        "Goals" -> {

        }

        "Saved Exercises" -> {
            navigateToScreen("SavedItemsScreen/gym")
        }
    }
}


fun handleYogaItemClick(
    clickedItem: String,
    navigateToScreen: (String) -> Unit
) {
    when (clickedItem) {
        "Track Workouts" -> {

        }

        "Activity" -> {

        }

        "Goals" -> {

        }

        "Saved Poses" -> {
            navigateToScreen("SavedItemsScreen/yoga")
        }
    }
}

@Composable
fun DialogToUpdate(
    showDialogToUpdateValueOf: String,
    profileViewModel: ProfileViewModel,
    hideDialog: () -> Unit,
    height: String,
    weightGoal: String,
    currentWeight: String,
    age: String,
    gender: String,
    caloriesGoal: String
) {
    when (showDialogToUpdateValueOf) {
        HelperFunctions.getPersonalInfoCategories()[0] -> {
            // Height
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = height,
                onConfirmClick = {
                    profileViewModel.saveHeight(it)
                },
                label = "Height (cm)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[1] -> {
            // "Weight",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = currentWeight,
                onConfirmClick = {
                    profileViewModel.saveCurrentWeight(it)
                },
                label = "Weight (kg)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[2] -> {
            //  "Gender",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = gender,
                onConfirmClick = {
                    profileViewModel.saveGender(it)
                },
                isGender = true,
                label = "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[3] -> {
            //  "BMI",
            return
        }

        HelperFunctions.getPersonalInfoCategories()[4] -> {
            //  "Age",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = age,
                onConfirmClick = {
                    profileViewModel.saveAge(it)
                },
                label = "Age",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[5] -> {
            //  "Weight Goal",
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = weightGoal,
                onConfirmClick = {
                    profileViewModel.saveWeightGoal(it)
                },
                label = "Weight Goal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        HelperFunctions.getPersonalInfoCategories()[6] -> {
            //  "Calories Goal"
            AlertDialogToUpdate(
                hideDialog = hideDialog,
                value = caloriesGoal,
                onConfirmClick = {
                    profileViewModel.saveCaloriesGoal(it)
                },
                label = "Calories Goal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogToUpdate(
    hideDialog: () -> Unit,
    value: String,
    onConfirmClick: (String) -> Unit,
    isGender: Boolean = false,
    label: String,
    keyboardOptions: KeyboardOptions,
    isText: Boolean = false
) {
    AlertDialog(
        onDismissRequest = {
            hideDialog()
        }
    ) {
        if (!isGender) {
            var newValue by remember {
                mutableStateOf(value)
            }
            var isEmpty by remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bottomBarColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    buildAnnotatedString {
                        append("Current ")
                        withStyle(
                            style = SpanStyle(
                                color = primaryTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(value)
                        }
                    },
                    color = primaryTextColor,
                    modifier = Modifier
                        .padding(LARGE_PADDING)
                )

                LandingPageOutlinedTextField(
                    label = label,
                    value = newValue,
                    onValueChange = {
                        if (isText) {
                            newValue = it
                        } else {
                            if (it.isDigitsOnly()) newValue = it
                        }
                        isEmpty = it.isBlank()
                    },
                    showErrorColor = isEmpty,
                    keyboardOptions = keyboardOptions,
                    isText = isText
                )

                Button(
                    onClick = {
                        if (newValue.isNotBlank()) {
                            onConfirmClick(newValue)
                            hideDialog()
                        }
                    },
                    modifier = Modifier
                        .padding(LARGE_PADDING)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorsUtil.primaryPurple
                    )
                ) {
                    Text(text = "Update", color = Color.White)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MEDIUM_PADDING)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bottomBarColor)
                    .padding(LARGE_PADDING)
            ) {
                GenderIcon(
                    resourceId = R.drawable.male,
                    selected = value == "Male",
                    onClick = {
                        onConfirmClick("Male")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.female,
                    selected = value == "Female",
                    onClick = {
                        onConfirmClick("Female")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.other_gender,
                    selected = value == "Other",
                    onClick = {
                        onConfirmClick("Other")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )
            }
        }
    }
}

fun getBmi(height: String, currentWeight: String): String {
    if (height.isNotBlank() && currentWeight.isNotBlank()) {
        val bmi = (currentWeight.toDouble() * 10_000) / (height.toDouble() * height.toDouble())
        return String.format("%.2f", bmi)
    }

    return "25"
}

@Composable
fun PersonalInformation(
    height: String,
    weight: String,
    bmi: String,
    gender: String,
    age: String,
    weightGoal: String,
    caloriesGoal: String,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = MEDIUM_PADDING, bottom = SMALL_PADDING, end = MEDIUM_PADDING)
            .clip(RoundedCornerShape(20.dp))
            .background(bottomBarColor)
    ) {
        HelperFunctions.getPersonalInfoCategories().forEachIndexed { index, category ->
            var categoryValue = ""
            when (index) {
                0 -> {
                    categoryValue = "$height cm"
                }

                1 -> {
                    categoryValue = "$weight kg"
                }

                2 -> {
                    categoryValue = gender
                }

                3 -> {
                    categoryValue = bmi
                }

                4 -> {
                    categoryValue = age
                }

                5 -> {
                    categoryValue = "$weightGoal kg"
                }

                6 -> {
                    categoryValue = caloriesGoal
                }
            }

            if (categoryValue == bmi) {
                PersonalInfoCategory(
                    category = category,
                    categoryValue = categoryValue,
                    categoryValueColor = if (bmi.toDouble() > 25 || bmi.toDouble() < 18) noAchievementColor else targetAchievedColor
                )
            } else {
                PersonalInfoCategory(
                    category = category,
                    categoryValue = categoryValue,
                    onClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun PersonalInfoCategory(
    category: String,
    categoryValue: String,
    categoryValueColor: Color = primaryTextColor,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SMALL_PADDING)
            .clickable {
                onClick(category)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = category,
            fontSize = 16.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(start = MEDIUM_PADDING, top = MEDIUM_PADDING, bottom = MEDIUM_PADDING)
                .weight(4f)
        )

        Text(
            text = categoryValue,
            color = categoryValueColor,
            modifier = Modifier
                .padding(end = MEDIUM_PADDING, top = MEDIUM_PADDING, bottom = MEDIUM_PADDING)
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 16.nonScaledSp,
            maxLines = 1
        )
    }
}

@Composable
fun SettingsCategory(
    text: String,
    onClick: () -> Unit,
    verticalLineColor: Color,
    showDivider: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.nonScaledSp,
                color = primaryTextColor,
                modifier = Modifier
                    .padding(MEDIUM_PADDING)
                    .weight(4f)
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = primaryTextColor,
                modifier = Modifier.weight(1f)
            )
        }

        if (showDivider) CustomDivider()
    }
}

@Composable
fun SettingsCategoryHeader(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .padding(MEDIUM_PADDING),
        color = primaryTextColor,
        fontSize = 20.nonScaledSp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun StickyHeaderTextContent(modifier: Modifier, text: String) {
    Surface(
        modifier = modifier,
        color = statusBarColor
    ) {
        Text(
            text = text,
            fontSize = 20.nonScaledSp,
            color = Color.White,
            modifier = Modifier.padding(vertical = SMALL_PADDING, horizontal = MEDIUM_PADDING)
        )
    }
}

@Composable
fun ProfilePictureAndUserName(
    name: String,
    onNameChange: (String) -> Unit
) {
    var shouldShowNameChangeDialog by remember {
        mutableStateOf(false)
    }

    if (shouldShowNameChangeDialog) {
        AlertDialogToUpdate(
            hideDialog = {
                shouldShowNameChangeDialog = false
            },
            value = name,
            onConfirmClick = {
                onNameChange(it)
            },
            label = "Your Name",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            isText = true
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(PROFILE_PICTURE_SIZE)
                .weight(1.3f)
                .clip(shape = CircleShape)
                .background(primaryBlue)
        ) {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    shouldShowNameChangeDialog = true
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 20.nonScaledSp,
                modifier = Modifier
                    .padding(SMALL_PADDING),
                color = primaryTextColor,
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(SMALL_PADDING))

            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = primaryTextColor,
                modifier = Modifier.size(LARGE_PADDING)
            )
        }
    }
}
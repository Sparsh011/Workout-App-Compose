package com.sparshchadha.workout_app.ui.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.CustomDivider
import com.sparshchadha.workout_app.ui.screens.workout.HeaderText
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
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
    val height = profileViewModel.readHeight.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val weightGoal = profileViewModel.readWeightGoal.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val currentWeight = profileViewModel.readCurrentWeight.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val gender = profileViewModel.readGender.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val age = profileViewModel.readAge.collectAsStateWithLifecycle(initialValue = "").value ?: ""
    val caloriesGoal = profileViewModel.readCaloriesGoal.collectAsStateWithLifecycle(initialValue = "").value ?: ""


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
            ProfilePictureAndUserName()
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
                caloriesGoal = caloriesGoal
            )
        }

        item {
            SettingsCategoryHeader(text = "Gym Workouts")
        }

        items(HelperFunctions.getGymWorkoutCategories()) {
            SettingsCategory(
                text = it,
                onClick = { navigateToRoute ->

                },
                verticalLineColor = primaryBlue
            )
        }

        item {
            SettingsCategoryHeader(text = "Yoga")
        }

        items(HelperFunctions.getYogaCategories()) {
            SettingsCategory(
                text = it,
                onClick = { navigateToRoute ->

                },
                verticalLineColor = noAchievementColor
            )
        }

        item {
            SettingsCategoryHeader(text = "Calories & Food")
        }

        items(HelperFunctions.getCaloriesTrackerCategories()) {
            SettingsCategory(
                text = it,
                onClick = { navigateToRoute ->

                },
                verticalLineColor = targetAchievedColor
            )
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
    caloriesGoal: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = MEDIUM_PADDING, bottom = SMALL_PADDING, end = MEDIUM_PADDING)
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
                    categoryValue = categoryValue
                )
            }
        }
    }
}

@Composable
fun PersonalInfoCategory(
    category: String,
    categoryValue: String,
    categoryValueColor: Color = primaryTextColor
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category,
            fontSize = 16.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(4f)
        )

        Text(
            text = categoryValue,
            color = categoryValueColor,
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsCategory(
    text: String,
    onClick: (String) -> Unit,
    verticalLineColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING, vertical = SMALL_PADDING)
            .clickable {
                onClick("")
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

        CustomDivider()
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
        fontSize = 20.nonScaledSp
    )
}

@Composable
fun StickyHeaderTextContent(modifier: Modifier, text: String) {
    Surface(
        modifier = modifier,
        color = ColorsUtil.statusBarColor
    ) {
        Text(
            text = text,
            fontSize = 20.nonScaledSp,
            color = primaryTextColor,
            modifier = Modifier.padding(MEDIUM_PADDING)
        )
    }
}

@Composable
fun ProfilePictureAndUserName() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .size(PROFILE_PICTURE_SIZE)
                .weight(1.5f)
                .clip(shape = CircleShape)
                .background(ColorsUtil.primaryBlue)
        ) {
            Image(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
            text = "Sparsh Chadha",
            fontSize = 24.nonScaledSp,
            modifier = Modifier
                .weight(5f)
                .padding(SMALL_PADDING),
            color = primaryTextColor
        )
    }
}
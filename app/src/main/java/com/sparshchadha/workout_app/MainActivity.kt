package com.sparshchadha.workout_app

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import androidx.navigation.compose.rememberNavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBar
import com.sparshchadha.workout_app.ui.navigation.destinations.NavGraph
import com.sparshchadha.workout_app.ui.theme.WorkoutAppTheme
import com.sparshchadha.workout_app.util.ColorsUtil.primaryPurple
import com.sparshchadha.workout_app.util.ColorsUtil.noAchievementColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.ColorsUtil.scaffoldBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.statusBarColor
import com.sparshchadha.workout_app.util.Dimensions.LARGE_PADDING
import com.sparshchadha.workout_app.util.Dimensions.MEDIUM_PADDING
import com.sparshchadha.workout_app.util.Dimensions.SMALL_PADDING
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WorkoutAppTheme {

                val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
                if (sharedPreferences.getBoolean("landing_page_shown", false)) {
                    val navHostController = rememberNavController()
                    val gymExercises = workoutViewModel.gymExercisesFromApi.value
                    val yogaPoses = workoutViewModel.yogaPosesFromApi.value

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
                    LandingPage(profileViewModel)
                }
            }
        }
    }

    private fun landingPageShown() {
        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("landing_page_shown", true)
        editor.apply()
    }

    @Composable
    fun LandingPage(
        profileViewModel: ProfileViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(scaffoldBackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Setup Your Profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusBarColor)
                    .padding(MEDIUM_PADDING),
                fontSize = 20.nonScaledSp,
                color = primaryTextColor
            )

            var ageStr by remember {
                mutableStateOf("")
            }

            var heightStr by remember {
                mutableStateOf("")
            }

            var weightStr by remember {
                mutableStateOf("")
            }

            var caloriesGoalStr by remember {
                mutableStateOf("")
            }

            var genderStr by remember {
                mutableStateOf("")
            }

            var weightGoalStr by remember {
                mutableStateOf("")
            }

            val context = LocalContext.current

            LandingPageOutlinedTextField(
                label = "Age",
                value = ageStr,
                onValueChange = {
                    ageStr = it
                }
            )

            LandingPageOutlinedTextField(
                label = "Height (CM)",
                value = heightStr,
                onValueChange = {
                    heightStr = it
                }
            )

            LandingPageOutlinedTextField(
                label = "Weight (Kg)",
                value = weightStr,
                onValueChange = {
                    weightStr = it
                }
            )

            LandingPageOutlinedTextField(
                label = "Calories Goal",
                value = caloriesGoalStr,
                onValueChange = {
                    caloriesGoalStr = it
                }
            )

            LandingPageOutlinedTextField(
                label = "Weight Goal",
                value = weightGoalStr,
                onValueChange = {
                    weightGoalStr = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MEDIUM_PADDING)
            ) {
                GenderIcon(
                    resourceId = R.drawable.male,
                    selected = genderStr == "Male",
                    onClick = {
                        genderStr = "Male"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.female,
                    selected = genderStr == "Female",
                    onClick = {
                        genderStr = "Female"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )

                GenderIcon(
                    resourceId = R.drawable.other_gender,
                    selected = genderStr == "Other",
                    onClick = {
                        genderStr = "Other"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(MEDIUM_PADDING)
                )
            }

            Button(
                onClick = {
                    if (areDetailsValid(
                            ageStr,
                            weightStr,
                            heightStr,
                            caloriesGoalStr,
                            genderStr,
                            weightGoalStr
                        )
                    ) {
                        Toast.makeText(
                            context,
                            "Good.",
                            Toast.LENGTH_SHORT
                        ).show()
                        profileViewModel.saveAllDetails(
                            height = heightStr,
                            age = ageStr,
                            weight = weightStr,
                            gender = genderStr,
                            weightGoal = weightGoalStr,
                            caloriesGoal = caloriesGoalStr
                        )
                        landingPageShown()
                    } else {
                        Toast.makeText(
                            context,
                            "Make Sure All Fields Are Correct And Filled.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(LARGE_PADDING)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryPurple
                )
            ) {
                Text(
                    text = "Proceed",
                    color = White
                )
            }
        }
    }

    private fun areDetailsValid(
        vararg args: String
    ): Boolean {
        for (arg in args) {
            if (arg.isBlank()) {
                return false
            }
        }

        return true
    }
}

@Composable
fun LandingPageOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showErrorColor: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    isText: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isText) {
                onValueChange(it)
            } else {
                if (it.isDigitsOnly()) onValueChange(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING, vertical = SMALL_PADDING),
        label = {
            Text(text = label, color = primaryTextColor)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = primaryTextColor,
            unfocusedTextColor = primaryTextColor,
            disabledTextColor = primaryTextColor,
            disabledBorderColor = if (showErrorColor) noAchievementColor else primaryBlue,
            unfocusedBorderColor = if (showErrorColor) noAchievementColor else primaryBlue,
            focusedBorderColor = if (showErrorColor) noAchievementColor else primaryBlue
        ),
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun GenderIcon(
    modifier: Modifier,
    resourceId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.clickable {
                onClick()
            }
        ) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(SMALL_PADDING))

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = primaryPurple,
                modifier = Modifier.size(
                    LARGE_PADDING
                )
            )
        }
    } else {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = modifier.clickable {
                onClick()
            }
        )
    }
}

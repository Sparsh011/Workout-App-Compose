package com.sparshchadha.workout_app.features.gym.presentation.gym

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.gym.domain.entities.GymExercisesEntity
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.DateRange
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel
import com.sparshchadha.workout_app.ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun GymActivityScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel
) {
    val performedExercises by workoutViewModel.allExercisesPerformed.collectAsStateWithLifecycle()
    val graphDateRange = workoutViewModel.dateRange.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getAllExercisesPerformed()
    }

    Scaffold(
        topBar = {
            ScaffoldTopBar(
                topBarDescription = "Gym Activity",
                onBackButtonPressed = { navController.popBackStack() }
            )
        }
    ) { innerPaddingValues ->
        if (performedExercises.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorsUtil.scaffoldBackgroundColor)
                    .padding(top = innerPaddingValues.calculateTopPadding()),
                contentAlignment = Alignment.Center
            ) {
                NoSavedItem(text = "Activity Data Not Available")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorsUtil.scaffoldBackgroundColor)
                    .padding(
                        top = innerPaddingValues.calculateTopPadding(),
                        start = Dimensions.SMALL_PADDING,
                        end = Dimensions.SMALL_PADDING,
                        bottom = Dimensions.SMALL_PADDING
                    )
                    .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                    .verticalScroll(rememberScrollState())
            ) {
                if (!performedExercises.isNullOrEmpty()) {
                    DateRangeTabRow(
                        currentDateRange = graphDateRange,
                        setDateRange = { newDateRange ->
                            workoutViewModel.setDateRange(newDateRange)
                        }
                    )
                    ActivityBarGraph(graphDateRange, performedExercises)
                    ExerciseStats(performedExercises, graphDateRange)
                }
            }
        }
    }
}

@Composable
fun ExerciseStats(
    performedExercises: List<GymExercisesEntity>?,
    dateRange: DateRange
) {
    if (performedExercises == null) return

    var mostPerformedExercisePair by remember {
        mutableStateOf(getMostPerformedExercise(performedExercises, dateRange))
    }
    var leastPerformedExercisePair by remember {
        mutableStateOf(getLeastPerformedExercise(performedExercises, dateRange))
    }
    var mostPerformedBodyPartPair by remember {
        mutableStateOf(getMostPerformedBodyPartPair(performedExercises, dateRange))
    }
    var leastPerformedBodyPartPair by remember {
        mutableStateOf(getLeastPerformedBodyPartPair(performedExercises, dateRange))
    }
    var mostActiveOn by remember {
        mutableStateOf(getMostActiveOnDay(performedExercises, dateRange))
    }

    when (dateRange) {
        DateRange.LAST_7_DAYS -> {
            mostPerformedExercisePair = getMostPerformedExercise(performedExercises, dateRange)
            leastPerformedExercisePair = getLeastPerformedExercise(performedExercises, dateRange)
            mostActiveOn = getMostActiveOnDay(performedExercises, dateRange)
            mostPerformedBodyPartPair = getMostPerformedBodyPartPair(performedExercises, dateRange)
            leastPerformedBodyPartPair = getLeastPerformedBodyPartPair(performedExercises, dateRange)
        }

        DateRange.LAST_30_DAYS -> {
            mostPerformedExercisePair = getMostPerformedExercise(performedExercises, dateRange)
            leastPerformedExercisePair = getLeastPerformedExercise(performedExercises, dateRange)
            mostActiveOn = getMostActiveOnDay(performedExercises, dateRange)
            mostPerformedBodyPartPair = getMostPerformedBodyPartPair(performedExercises, dateRange)
            leastPerformedBodyPartPair = getLeastPerformedBodyPartPair(performedExercises, dateRange)
        }

        DateRange.LAST_6_MONTHS -> {
            mostPerformedExercisePair = getMostPerformedExercise(performedExercises, dateRange)
            leastPerformedExercisePair = getLeastPerformedExercise(performedExercises, dateRange)
            mostActiveOn = getMostActiveOnDay(performedExercises, dateRange)
            mostPerformedBodyPartPair = getMostPerformedBodyPartPair(performedExercises, dateRange)
            leastPerformedBodyPartPair = getLeastPerformedBodyPartPair(performedExercises, dateRange)
        }
    }

    MostOrLeastPerformedExercise(
        performedExercisePair = mostPerformedExercisePair,
        dateRange = dateRange,
        isMostPerformed = true,
        heading = "Most Performed Exercise"
    )

    MostOrLeastPerformedExercise(
        performedExercisePair = leastPerformedExercisePair,
        dateRange = dateRange,
        isMostPerformed = false,
        heading = "Least Performed Exercise"
    )

    MostOrLeastPerformedExercise(
        performedExercisePair = mostPerformedBodyPartPair,
        dateRange = dateRange,
        isMostPerformed = true,
        heading = "Most Trained Muscle"
    )

    MostOrLeastPerformedExercise(
        performedExercisePair = leastPerformedBodyPartPair,
        dateRange = dateRange,
        isMostPerformed = false,
        heading = "Least Trained Muscle"
    )

    MostActiveOn(
        heading = "Most active on",
        mostActiveOn = mostActiveOn
    )
}

fun getLeastPerformedBodyPartPair(
    performedExercises: List<GymExercisesEntity>,
    dateRange: DateRange
): Pair<String, Int> {
    val now = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> now.minusDays(7)
        DateRange.LAST_30_DAYS -> now.minusDays(30)
        DateRange.LAST_6_MONTHS -> now.minusMonths(6)
    }
    var minSets = Int.MAX_VALUE
    var result = Pair("", minSets)
    val exercisesMap = hashMapOf<String, Int>()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    for (exercise in performedExercises) {
        val dateString = "%02d-%02d-%04d".format(
            exercise.date.toInt(),
            HelperFunctions.getMonthIndexFromName(exercise.month),
            now.year
        )
        val exerciseDate = LocalDate.parse(dateString, dateFormatter)
        if (exerciseDate.isBefore(startDate)) continue

        exercisesMap[exercise.exerciseDetails?.muscle ?: "Unknown name"] =
            exercise.setsPerformed + (exercisesMap[exercise.exerciseDetails?.muscle] ?: 0)
        if (minSets > (exercisesMap[exercise.exerciseDetails?.muscle] ?: 0)) {
            minSets = exercisesMap[exercise.exerciseDetails?.muscle]!!
            result = Pair(exercise.exerciseDetails?.muscle ?: "Unknown", minSets)
        }
    }

    return result
}

fun getMostPerformedBodyPartPair(
    performedExercises: List<GymExercisesEntity>,
    dateRange: DateRange
): Pair<String, Int> {
    val now = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> now.minusDays(7)
        DateRange.LAST_30_DAYS -> now.minusDays(30)
        DateRange.LAST_6_MONTHS -> now.minusMonths(6)
    }

    var maxSets = Int.MIN_VALUE
    var result = Pair("", maxSets)
    val exercisesMap = hashMapOf<String, Int>()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    for (exercise in performedExercises) {
        val dateString = "%02d-%02d-%04d".format(
            exercise.date.toInt(),
            HelperFunctions.getMonthIndexFromName(exercise.month),
            now.year
        )
        val exerciseDate = LocalDate.parse(dateString, dateFormatter)
        if (exerciseDate.isBefore(startDate)) continue

        val muscleTarget = exercise.exerciseDetails?.muscle ?: "Unknown name"
        exercisesMap[muscleTarget] = exercise.setsPerformed + (exercisesMap[muscleTarget] ?: 0)

        if (maxSets < exercisesMap[muscleTarget]!!) {
            maxSets = exercisesMap[muscleTarget]!!
            result = Pair(muscleTarget, maxSets)
        }
    }

    return result
}

private fun getMostPerformedExercise(
    performedExercises: List<GymExercisesEntity>,
    dateRange: DateRange
): Pair<String, Int> {
    val now = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> now.minusDays(7)
        DateRange.LAST_30_DAYS -> now.minusDays(30)
        DateRange.LAST_6_MONTHS -> now.minusMonths(6)
    }

    var maxSets = Int.MIN_VALUE
    var result = Pair("", maxSets)
    val exercisesMap = hashMapOf<String, Int>()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    for (exercise in performedExercises) {
        val dateString = "%02d-%02d-%04d".format(
            exercise.date.toInt(),
            HelperFunctions.getMonthIndexFromName(exercise.month),
            now.year
        )
        val exerciseDate = LocalDate.parse(dateString, dateFormatter)
        if (exerciseDate.isBefore(startDate)) continue

        val exerciseName = exercise.exerciseDetails?.name ?: "Unknown name"
        exercisesMap[exerciseName] = exercise.setsPerformed + (exercisesMap[exerciseName] ?: 0)

        if (maxSets < exercisesMap[exerciseName]!!) {
            maxSets = exercisesMap[exerciseName]!!
            result = Pair(exerciseName, maxSets)
        }
    }

    return result
}

private fun getLeastPerformedExercise(
    performedExercises: List<GymExercisesEntity>,
    dateRange: DateRange
): Pair<String, Int> {
    val now = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> now.minusDays(7)
        DateRange.LAST_30_DAYS -> now.minusDays(30)
        DateRange.LAST_6_MONTHS -> now.minusMonths(6)
    }
    var minSets = Int.MAX_VALUE
    var result = Pair("", minSets)
    val exercisesMap = hashMapOf<String, Int>()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    for (exercise in performedExercises) {
        val dateString = "%02d-%02d-%04d".format(
            exercise.date.toInt(),
            HelperFunctions.getMonthIndexFromName(exercise.month),
            now.year
        )
        val exerciseDate = LocalDate.parse(dateString, dateFormatter)
        if (exerciseDate.isBefore(startDate)) continue

        exercisesMap[exercise.exerciseDetails?.name ?: "Unknown name"] =
            exercise.setsPerformed + (exercisesMap[exercise.exerciseDetails?.name] ?: 0)
        if (minSets > (exercisesMap[exercise.exerciseDetails?.name] ?: 0)) {
            minSets = exercisesMap[exercise.exerciseDetails?.name]!!
            result = Pair(exercise.exerciseDetails?.name ?: "Unknown", minSets)
        }
    }

    return result
}

private fun getMostActiveOnDay(
    performedExercises: List<GymExercisesEntity>,
    dateRange: DateRange
): Pair<Pair<String, String>, Int> {
    var maxSets = Int.MIN_VALUE
    var result = Pair(Pair("", ""), maxSets)
    val now = LocalDate.now()
    val startDate = when (dateRange) {
        DateRange.LAST_7_DAYS -> now.minusDays(7)
        DateRange.LAST_30_DAYS -> now.minusDays(30)
        DateRange.LAST_6_MONTHS -> now.minusMonths(6)
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    for (exercise in performedExercises) {
        val dateString = "%02d-%02d-%04d".format(
            exercise.date.toInt(),
            HelperFunctions.getMonthIndexFromName(exercise.month),
            now.year
        )
        val exerciseDate = LocalDate.parse(dateString, dateFormatter)

        if (exerciseDate.isBefore(startDate)) continue

        if (exercise.setsPerformed > maxSets) {
            maxSets = exercise.setsPerformed
            result = Pair(
                Pair(exercise.exerciseDetails?.name ?: "Unknown", "$exerciseDate"),
                maxSets
            )
        }
    }

    return result
}

fun getSelectedTab(currentDateRange: DateRange): Int {
    return when (currentDateRange) {
        DateRange.LAST_7_DAYS -> 0
        DateRange.LAST_30_DAYS -> 1
        DateRange.LAST_6_MONTHS -> 2
    }
}

@Composable
fun DateRangeTabRow(
    currentDateRange: DateRange,
    setDateRange: (DateRange) -> Unit,
) {
    val selectedTab = getSelectedTab(currentDateRange)
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = ColorsUtil.scaffoldBackgroundColor,
        indicator = { tabPositions ->
            if (selectedTab < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .clip(
                            RoundedCornerShape(
                                topEnd = Dimensions.LARGE_PADDING,
                                topStart = Dimensions.LARGE_PADDING
                            )
                        ),
                    color = ColorsUtil.primaryBlue
                )
            }
        },
        tabs = {
            DateTab(
                title = "Last 7 days",
                isSelected = selectedTab == 0
            ) {
                setDateRange(DateRange.LAST_7_DAYS)
            }
            DateTab(
                title = "Last 30 days",
                isSelected = selectedTab == 1
            ) {
                setDateRange(DateRange.LAST_30_DAYS)
            }
            DateTab(
                title = "Last 6 months",
                isSelected = selectedTab == 2
            ) {
                setDateRange(DateRange.LAST_6_MONTHS)
            }
        }
    )

    Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))
}

@Composable
private fun DateTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Tab(
        selected = isSelected,
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = title,
            color = ColorsUtil.primaryTextColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .padding(Dimensions.MEDIUM_PADDING)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun MostOrLeastPerformedExercise(
    performedExercisePair: Pair<String, Int>,
    dateRange: DateRange,
    isMostPerformed: Boolean,
    heading: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM_PADDING),
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.bottomBarColor
        )
    ) {
        Header(heading)

        AnnotatedBody(
            buildAnnotatedString {
                append("${performedExercisePair.first}, ")
                withStyle(
                    style = SpanStyle(
                        color = if (isMostPerformed) ColorsUtil.targetAchievedColor else ColorsUtil.noAchievementColor,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("${performedExercisePair.second} sets")
                }
                append(" performed in ${dateRange.name.lowercase().replace('_', ' ')}")
            },
        )

        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))
    }
}

@Composable
fun MostActiveOn(
    heading: String,
    mostActiveOn: Pair<Pair<String, String>, Int>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM_PADDING),
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.bottomBarColor
        )
    ) {
        Header(heading = heading)

        AnnotatedBody(
            annotatedString = buildAnnotatedString {
                append("Performed ")
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.targetAchievedColor,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("${mostActiveOn.second} ")
                }
                append("sets of ")
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.targetAchievedColor,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("${mostActiveOn.first.first} ")

                }
                append("on ")
                withStyle(
                    style = SpanStyle(
                        color = ColorsUtil.targetAchievedColor,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append("${mostActiveOn.first.second} ")
                }
            }
        )

        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))
    }
}

@Composable
private fun Header(heading: String) {
    Text(
        text = heading,
        color = ColorsUtil.primaryTextColor,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(Dimensions.MEDIUM_PADDING),
        fontSize = 22.nonScaledSp
    )
}

@Composable
private fun AnnotatedBody(
    annotatedString: AnnotatedString
) {
    Text(
        annotatedString,
        color = ColorsUtil.primaryTextColor,
        modifier = Modifier.padding(horizontal = Dimensions.MEDIUM_PADDING),
        fontSize = 16.nonScaledSp
    )
}

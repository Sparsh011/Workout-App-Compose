package com.sparshchadha.workout_app.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.features.yoga.data.remote.dto.Pose
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.MuscleType
import com.sparshchadha.workout_app.features.gym.presentation.gym.util.WorkoutType
import com.sparshchadha.workout_app.ui.screens.workout.DifficultyLevel
import com.sparshchadha.workout_app.util.Extensions.capitalize
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.Locale


object HelperFunctions {
    fun getDifficultyLevels(): List<String> {
        return listOf(
            DifficultyLevel.BEGINNER.name.lowercase().capitalize(),
            DifficultyLevel.INTERMEDIATE.name.lowercase().capitalize(),
            DifficultyLevel.EXPERT.name.lowercase().capitalize()
        )
    }

    fun getMuscleTypes(): List<String> {
        return listOf(
            MuscleType.ABDOMINALS.name.lowercase().capitalize(),
            MuscleType.ABDUCTORS.name.lowercase().capitalize(),
            MuscleType.ADDUCTORS.name.lowercase().capitalize(),
            MuscleType.BICEPS.name.lowercase().capitalize(),
            MuscleType.CALVES.name.lowercase().capitalize(),
            MuscleType.CHEST.name.lowercase().capitalize(),
            MuscleType.FOREARMS.name.lowercase().capitalize(),
            MuscleType.GLUTES.name.lowercase().capitalize(),
            MuscleType.HAMSTRINGS.name.lowercase().capitalize(),
            MuscleType.LATS.name.lowercase().capitalize(),
            MuscleType.LOWER_BACK.name.lowercase().capitalize().replace('_', ' '),
            MuscleType.MIDDLE_BACK.name.lowercase().capitalize().replace('_', ' '),
            MuscleType.NECK.name.lowercase().capitalize(),
            MuscleType.QUADRICEPS.name.lowercase().capitalize(),
            MuscleType.TRAPS.name.lowercase().capitalize(),
            MuscleType.TRICEPS.name.lowercase().capitalize(),
        )
    }

    fun getWorkoutTypes(): List<String> {
        return listOf(
            WorkoutType.CARDIO.name.lowercase().capitalize(),
            WorkoutType.OLYMPIC_WEIGHTLIFTING.name.lowercase().capitalize().replace('_', ' '),
            WorkoutType.PLYOMETRICS.name.lowercase().capitalize(),
            WorkoutType.POWERLIFTING.name.lowercase().capitalize(),
            WorkoutType.STRENGTH.name.lowercase().capitalize(),
            WorkoutType.STRETCHING.name.lowercase().capitalize(),
            WorkoutType.STRONGMAN.name.lowercase().capitalize(),
        )
    }

    fun getNumberOfSetsOrQuantity(): List<String> {
        return listOf("1", "2", "3", "4", "5")
    }

    fun getCurrentDateAndMonth(): Pair<Int, String> {
        val currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

        return Pair(currentDate, currentMonth)
    }

    // returns Date, Month
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLast30Days(): MutableList<Pair<Int, String>> {
        val (currentDate, currentMonth) = getCurrentDateAndMonth()
        val monthMap = getMonthMap()
        val daysInMonthMap = daysInMonthMap()
        val last30DaysList = mutableListOf<Pair<Int, String>>()

        if (currentDate < 30) {
            // Add current month's past days
            for (day in currentDate downTo 1) {
                last30DaysList.add(Pair(first = day, second = currentMonth))
            }

            val previousMonth = getPreviousMonth(monthMap[currentMonth])
            val remainingDays = 30 - currentDate
            val daysInPrevMonth = daysInMonthMap[previousMonth] ?: 0

            // Add previous month's valid days
            for (day in daysInPrevMonth downTo (daysInPrevMonth - remainingDays + 1)) {
                last30DaysList.add(Pair(first = day, second = previousMonth))
            }
        } else {
            for (day in currentDate downTo 1) {
                last30DaysList.add(Pair(first = day, second = currentMonth))
            }
        }

        return last30DaysList
    }

    private fun getMonthMap(): Map<String, Int> {
        return mapOf(
            "January" to 1,
            "February" to 2,
            "March" to 3,
            "April" to 4,
            "May" to 5,
            "June" to 6,
            "July" to 7,
            "August" to 8,
            "September" to 9,
            "October" to 10,
            "November" to 11,
            "December" to 12
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthMap(): Map<String, Int> {
        val currentYear = LocalDate.now().year

        return mapOf(
            "January" to 31,
            "February" to if (Year.isLeap(currentYear.toLong())) 29 else 28,
            "March" to 31,
            "April" to 30,
            "May" to 31,
            "June" to 30,
            "July" to 31,
            "August" to 31,
            "September" to 30,
            "October" to 31,
            "November" to 30,
            "December" to 31
        )
    }


    private fun getPreviousMonth(currentMonthIndex: Int?): String {
        return when (currentMonthIndex) {
            1 -> "December"
            2 -> "January"
            3 -> "February"
            4 -> "March"
            5 -> "April"
            6 -> "May"
            7 -> "June"
            8 -> "July"
            9 -> "August"
            10 -> "September"
            11 -> "October"
            else -> "November"
        }
    }

    private fun getNextMonth(currentMonthIndex: Int?): String {
        return when (currentMonthIndex) {
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> "January"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNext2Days(): MutableList<Pair<Int, String>> {
        val (currentDate, currentMonth) = getCurrentDateAndMonth()
        val monthMap = getMonthMap()
        val daysInMonthMap = daysInMonthMap()
        val next3DaysList = mutableListOf<Pair<Int, String>>()
        val daysInCurrentMonth = daysInMonthMap[currentMonth] ?: 31

        if (daysInCurrentMonth - currentDate >= 2) {
            next3DaysList.add(currentDate + 1 to currentMonth)
            next3DaysList.add(currentDate + 2 to currentMonth)
        } else {
            val nextMonth = getNextMonth(monthMap[currentMonth])

            val validDaysForNextMonth = 2 - (daysInCurrentMonth - currentDate)
            for (day in currentDate + 1..daysInCurrentMonth) {
                next3DaysList.add(day to currentMonth)
            }

            for (day in 1..validDaysForNextMonth) {
                next3DaysList.add(day to nextMonth)
            }
        }

        return next3DaysList
    }

    fun getAchievementColor(achieved: Int, target: Int): Color {
        return if (achieved <= 0) {
            ColorsUtil.noAchievementColor
        } else if (achieved < target) {
            ColorsUtil.partialTargetAchievedColor
        } else {
            ColorsUtil.targetAchievedColor
        }
    }

    fun getMonthFromIndex(index: Int?): String {
        return when (index) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> {
                "Invalid Month"
            }
        }
    }

    fun getMonthIndexFromName(name: String?): Int {
        return when (name) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> -1
        }
    }

    fun getBack(): List<String> {
        return listOf(
            MuscleType.LATS.name.lowercase().capitalize(),
            MuscleType.MIDDLE_BACK.name.lowercase().capitalize().replace('_', ' '),
            MuscleType.LOWER_BACK.name.lowercase().capitalize().replace('_', ' ')
        )
    }

    fun getChestShouldersAndNeck(): List<String> {
        return listOf(
            MuscleType.CHEST.name.lowercase().capitalize(),
            MuscleType.TRAPS.name.lowercase().capitalize(),
            MuscleType.NECK.name.lowercase().capitalize()
        )
    }

    fun getArms(): List<String> {
        return listOf(
            MuscleType.BICEPS.name.lowercase().capitalize(),
            MuscleType.TRICEPS.name.lowercase().capitalize(),
            MuscleType.FOREARMS.name.lowercase().capitalize(),
        )
    }

    fun getLegs(): List<String> {
        return listOf(
            MuscleType.GLUTES.name.lowercase().capitalize(),
            MuscleType.CALVES.name.lowercase().capitalize(),
            MuscleType.HAMSTRINGS.name.lowercase().capitalize(),
            MuscleType.QUADRICEPS.name.lowercase().capitalize()
        )
    }

    fun getAbs(): List<String> {
        return listOf(
            MuscleType.ABDUCTORS.name.lowercase().capitalize(),
            MuscleType.ADDUCTORS.name.lowercase().capitalize(),
            MuscleType.ABDOMINALS.name.lowercase().capitalize()
        )
    }

    fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }

    fun settingsForGym(): List<String> {
        return listOf(
            "Track Workouts",
            "Activity",
            "Personal Records",
            "Goals",
            "Saved Exercises"
        )
    }

    fun settingsForYoga(): List<String> {
        return listOf(
            "Track Workouts",
            "Activity",
            "Saved Poses",
        )
    }

    fun settingsForCalorieTracker(): List<String> {
        return listOf(
            "Track Food",
            "Activity",
            "Saved Food Items",
            "Your Dishes"
        )
    }

    fun getPersonalInfoCategories(): List<String> {
        return listOf(
            "Height",
            "Weight",
            "Gender",
            "BMI",
            "Age",
            "Weight Goal",
            "Calories Goal"
        )
    }

    fun getYogaPoseWithNegatives(pose: Pose): YogaEntity {
        return YogaEntity(
            date = "",
            month = "",
            setsPerformed = -1,
            yogaPoseDetails = pose,
            hour = -1,
            minutes = -1,
            seconds = -1,
        )
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }

        return true
    }
}
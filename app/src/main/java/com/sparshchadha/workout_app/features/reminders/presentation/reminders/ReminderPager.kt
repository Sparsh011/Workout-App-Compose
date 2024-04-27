package com.sparshchadha.workout_app.features.reminders.presentation.reminders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.sparshchadha.workout_app.R
import com.sparshchadha.workout_app.features.reminders.domain.entity.ReminderEntity
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions
import java.time.LocalDateTime
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodAndExerciseReminderPager(
    pagerState: PagerState,
    foodReminders: List<ReminderEntity>?,
    workoutReminders: List<ReminderEntity>?,
    globalPaddingValues: PaddingValues,
    deleteReminder: (ReminderEntity) -> Unit,
    localPaddingValues: PaddingValues,
    waterReminders: List<ReminderEntity>?
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                Reminders(
                    reminders = workoutReminders,
                    localPaddingValues = localPaddingValues,
                    globalPaddingValues = globalPaddingValues,
                    deleteReminder = deleteReminder
                )
            }

            1 -> {
                Reminders(
                    reminders = foodReminders,
                    localPaddingValues = localPaddingValues,
                    globalPaddingValues = globalPaddingValues,
                    deleteReminder = deleteReminder
                )
            }

            2 -> {
                Reminders(
                    reminders = waterReminders,
                    localPaddingValues = localPaddingValues,
                    globalPaddingValues = globalPaddingValues,
                    deleteReminder = deleteReminder
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Reminders(
    reminders: List<ReminderEntity>?,
    globalPaddingValues: PaddingValues,
    localPaddingValues: PaddingValues,
    deleteReminder: (ReminderEntity) -> Unit,
) {

    if (reminders != null) {
        val currentDateTime = LocalDateTime.now()
        val verticalArrangement = if (reminders.isEmpty()) Arrangement.Center else Arrangement.Top


        val (pastReminders, upcomingReminders) = reminders.partition { reminder ->
            val reminderDateTime = LocalDateTime.of(
                reminder.year.toInt(),
                Month.valueOf(
                    HelperFunctions.getMonthFromIndex(reminder.month.toInt()).uppercase()
                ).value,
                reminder.date.toInt(),
                reminder.hours,
                reminder.minutes
            )
            reminderDateTime < currentDateTime
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = verticalArrangement
        ) {

            if (pastReminders.isNotEmpty()) {
                stickyHeader {
                    Text(
                        buildAnnotatedString {
                            append("Past")
                            withStyle(
                                style = SpanStyle(
                                    color = ColorsUtil.primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.nonScaledSp,
                                )
                            ) {
                                append(" Reminders")
                            }
                        },
                        fontSize = 20.nonScaledSp,
                        color = ColorsUtil.primaryTextColor,
                        modifier = Modifier.padding(Dimensions.MEDIUM_PADDING)
                    )
                }

                items(
                    items = pastReminders,
                    key = { it.id.toString() }
                ) { reminder ->
                    Reminder(
                        reminder = reminder,
                        deleteReminder = deleteReminder,
                        deleteIconColor = ColorsUtil.targetAchievedColor
                    )
                }
            }

            if (upcomingReminders.isNotEmpty()) {
                stickyHeader {
                    Text(
                        buildAnnotatedString {
                            append("Upcoming")
                            withStyle(
                                style = SpanStyle(
                                    color = ColorsUtil.primaryTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.nonScaledSp,
                                )
                            ) {
                                append(" Reminders")
                            }
                        },
                        fontSize = 20.nonScaledSp,
                        color = ColorsUtil.primaryTextColor,
                        modifier = Modifier.padding(Dimensions.MEDIUM_PADDING)
                    )
                }

                items(
                    items = upcomingReminders,
                    key = { it.id.toString() }
                ) { reminder ->
                    Reminder(
                        reminder = reminder,
                        deleteReminder = deleteReminder,
                        deleteIconColor = ColorsUtil.noAchievementColor
                    )
                }
            }

            if (upcomingReminders.isEmpty() && pastReminders.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_svg),
                            contentDescription = null,
                            tint = ColorsUtil.noAchievementColor,
                            modifier = Modifier.size(Dimensions.PIE_CHART_SIZE)
                        )

                        Spacer(modifier = Modifier.height(Dimensions.MEDIUM_PADDING))

                        Text(
                            text = "No Reminders",
                            fontSize = 18.nonScaledSp,
                            color = ColorsUtil.primaryTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Reminder(
    reminder: ReminderEntity,
    deleteReminder: (ReminderEntity) -> Unit,
    deleteIconColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM_PADDING)
            .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
            .background(ColorsUtil.cardBackgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
        ) {
            Text(
                text = reminder.reminderDescription.capitalize(),
                fontSize = 20.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                color = ColorsUtil.primaryTextColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = Dimensions.SMALL_PADDING, horizontal = Dimensions.MEDIUM_PADDING)
            )

            val month by remember {
                mutableStateOf(
                    if (reminder.month.isNotBlank()) reminder.month.toInt().toString() else ""
                )
            }

            Text(
                buildAnnotatedString {
                    append(
                        "${reminder.date} ${
                            HelperFunctions.getMonthFromIndex(month.toInt()).substring(0..2)
                        } ${reminder.year}, "
                    )
                    withStyle(
                        style = SpanStyle(
                            color = ColorsUtil.primaryTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.nonScaledSp,
                        )
                    ) {
                        val hours = convertTo12HourFormat(reminder.hours)
                        val minutes =
                            if (reminder.minutes < 10) "0${reminder.minutes}" else reminder.minutes.toString()
                        append("${hours.first}: $minutes ${hours.second}")
                    }
                },
                fontSize = 14.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(vertical = Dimensions.SMALL_PADDING, horizontal = Dimensions.MEDIUM_PADDING),
                color = ColorsUtil.primaryTextColor
            )

            Text(
                buildAnnotatedString {
                    append("Reminder For ")
                    withStyle(
                        style = SpanStyle(
                            color = ColorsUtil.primaryTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.nonScaledSp,
                        )
                    ) {
                        append(reminder.reminderType.lowercase().capitalize())
                    }
                },
                fontSize = 14.nonScaledSp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = ColorsUtil.primaryTextColor,
                modifier = Modifier.padding(
                    top = Dimensions.SMALL_PADDING,
                    bottom = Dimensions.MEDIUM_PADDING,
                    end = Dimensions.MEDIUM_PADDING,
                    start = Dimensions.MEDIUM_PADDING
                ),
            )
        }

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    deleteReminder(reminder)
                },
            tint = deleteIconColor
        )
    }
}

fun convertTo12HourFormat(hours24: Int): Pair<Int, String> {
    var hours12 = hours24 % 12
    if (hours12 == 0)
        hours12 = 12
    val period = if (hours24 < 12) "AM" else "PM"
    return Pair(hours12, period)
}
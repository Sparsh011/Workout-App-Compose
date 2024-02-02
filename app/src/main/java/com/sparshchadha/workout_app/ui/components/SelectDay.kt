package com.sparshchadha.workout_app.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions.ACHIEVEMENT_INDICATOR_COLOR_SIZE
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.util.HelperFunctions

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarRow(
    getResultsForDateAndMonth: (Pair<Int, String>) -> Unit,
    selectedMonth: String,
    selectedDay: Int,
    indicatorColor: Color = Color.Red,
) {
    val last30Days = HelperFunctions.getLast30Days()
    last30Days.reverse()

    val lazyRowState = rememberLazyListState()
    LaunchedEffect(
        key1 = true,
        block = {
            lazyRowState.scrollToItem(last30Days.size - 3)
        }
    )

    val getNext2Days = HelperFunctions.getNext2Days()

    BoxWithConstraints {
        val width = maxWidth / 5

        LazyRow(
            state = lazyRowState,
            modifier = Modifier.background(Color.White)
        ) {
            items(last30Days) {
                if (
                    selectedDayAndMonth(
                        selectedDay = selectedDay,
                        selectedMonth = selectedMonth,
                        pair = it
                    )
                ) {
                    DayAndDate(
                        isSelected = true,
                        date = it.first.toString(),
                        month = it.second.substring(0..2),
                        modifier = Modifier
                            .clickable {
                                getResultsForDateAndMonth(Pair(it.first, it.second))
                            }
                            .width(width),
                        indicatorColor = indicatorColor
                    )
                } else {
                    DayAndDate(
                        modifier = Modifier
                            .clickable {
                                getResultsForDateAndMonth(Pair(it.first, it.second))
                            }
                            .width(width),
                        date = it.first.toString(),
                        month = it.second.substring(0..2)
                    )
                }
            }

            items(getNext2Days) {
                DayAndDate(
                    modifier = Modifier
                        .width(width),
                    date = it.first.toString(),
                    month = it.second.substring(0..2),
                    monthColor = ColorsUtil.unselectedBottomBarIconColor,
                    dateColor = ColorsUtil.unselectedBottomBarIconColor,
                    indicatorColor = ColorsUtil.unselectedBottomBarIconColor
                )
            }
        }
    }
}

private fun selectedDayAndMonth(selectedDay: Int, selectedMonth: String, pair: Pair<Int, String>): Boolean {
    return selectedMonth == pair.second && selectedDay == pair.first
}

@Composable
private fun DayAndDate(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    date: String,
    month: String,
    monthColor: Color = Color.Black,
    dateColor: Color = ColorsUtil.primaryDarkTextColor,
    indicatorColor: Color = Color.Transparent,
) {
    if (isSelected) {
        Column(
            modifier = modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ColorsUtil.primaryDarkTextColor)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = month,
                fontSize = 15.nonScaledSp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = date,
                fontSize = 14.nonScaledSp,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.size(10.dp))

            Canvas(
                modifier = Modifier
                    .size(ACHIEVEMENT_INDICATOR_COLOR_SIZE)
            ) {
                drawCircle(color = indicatorColor)
            }
        }
    } else {
        Column(
            modifier = modifier
                .background(Color.White)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = month,
                fontSize = 16.nonScaledSp,
                fontWeight = FontWeight.Bold,
                color = monthColor,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = date,
                fontSize = 14.nonScaledSp,
                color = dateColor,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}
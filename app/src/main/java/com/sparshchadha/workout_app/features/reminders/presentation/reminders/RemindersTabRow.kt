package com.sparshchadha.workout_app.features.reminders.presentation.reminders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun RemindersTabRow(
    selectedTabIndex: Int,
    tabBarHeadings: List<String>,
    updateSelectedTabIndex: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth(),
        divider = {
            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .padding(horizontal = Dimensions.MEDIUM_PADDING)
            )
        },
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex])
                    .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                    .padding(horizontal = Dimensions.MEDIUM_PADDING),
                color = ColorsUtil.primaryBlue
            )
        },
        containerColor = ColorsUtil.scaffoldBackgroundColor
    ) {
        tabBarHeadings.forEachIndexed { index, heading ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    updateSelectedTabIndex(index)
                },
                text = {
                    Text(
                        text = heading,
                        color = ColorsUtil.primaryTextColor,
                        fontSize = 20.nonScaledSp
                    )
                },
                modifier = Modifier.fillMaxWidth(1f)
            )
        }
    }
}
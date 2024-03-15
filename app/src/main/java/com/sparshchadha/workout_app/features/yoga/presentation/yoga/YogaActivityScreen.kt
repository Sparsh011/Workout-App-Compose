package com.sparshchadha.workout_app.features.yoga.presentation.yoga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sparshchadha.workout_app.features.yoga.domain.entities.YogaEntity
import com.sparshchadha.workout_app.ui.components.shared.NoSavedItem
import com.sparshchadha.workout_app.ui.components.shared.ScaffoldTopBar
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Dimensions
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp
import com.sparshchadha.workout_app.features.gym.presentation.viewmodels.WorkoutViewModel

@Composable
fun YogaActivityScreen(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    globalPaddingValues: PaddingValues
) {
    val performedYogaPoses by workoutViewModel.allYogaPosesPerformed.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        workoutViewModel.getAllYogaPosesPerformed()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = globalPaddingValues.calculateBottomPadding())
    ) {
        ScaffoldTopBar(
            topBarDescription = "All Poses Performed",
            onBackButtonPressed = {
                navController.popBackStack()
            }
        )

        // Add graph here to show number of sets performed on each day

        if (!performedYogaPoses.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.SMALL_PADDING)
                    .clip(RoundedCornerShape(Dimensions.MEDIUM_PADDING))
                    .background(ColorsUtil.bottomBarColor)
            ) {
                items(performedYogaPoses ?: emptyList()) {
                    PosePerformed(
                        pose = it,
                        onClick = {

                        }
                    )
                }
            }
        } else {
            NoSavedItem()
        }
    }
}

@Composable
fun PosePerformed(
    pose: YogaEntity,
    onClick: () -> Unit
) {
    Text(
        text = "${pose.yogaPoseDetails?.english_name}, ${pose.setsPerformed} sets",
        color = ColorsUtil.primaryTextColor,
        fontSize = 18.nonScaledSp,
        modifier = Modifier
            .padding(Dimensions.MEDIUM_PADDING)
            .clickable {
                onClick()
            }
    )
}

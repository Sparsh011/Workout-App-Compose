package com.sparshchadha.workout_app.ui.screens.calorie_tracker

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBackgroundColor
import com.sparshchadha.workout_app.util.ColorsUtil.primaryBlue
import com.sparshchadha.workout_app.util.ColorsUtil.primaryDarkGray
import com.sparshchadha.workout_app.util.ColorsUtil.primaryFoodCardBackground
import com.sparshchadha.workout_app.util.ColorsUtil.primaryGreen


@Composable
fun CalorieTrackerComposable(navController: NavHostController) {
    CalorieTrackerScreen(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieTrackerScreen(navController: NavHostController) {
    var caloriesGoal by remember {
        mutableFloatStateOf(1000F)
    }

    var caloriesConsumedToday by remember {
        mutableFloatStateOf(0F)
    }

    var showCaloriesGoalBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()

    var searchBarQuery by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(primaryBackgroundColor)
    ) {

        // Search dishes to add calories
        item {
            SearchBar(
                query = searchBarQuery,
                onQueryChange = {
                    searchBarQuery = it
                },
                onSearch = {
                    Toast.makeText(context, "Searching for $it", Toast.LENGTH_SHORT).show()
                },
                active = false,
                onActiveChange = {
                    navController.navigate("SearchScreen/food")
                },
                placeholder = {
                    Text(text = "Search Your Dish...", color = primaryDarkGray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = primaryDarkGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            searchBarQuery = ""
                            focusManager.clearFocus()
                        },
                        tint = primaryDarkGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = primaryBackgroundColor,
                    inputFieldColors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = primaryFoodCardBackground
                    )
                ),
                shape = RoundedCornerShape(size = 10.dp)
            ) {

            }
        }

        item {
            // Calories consumed today
            var progress: Float by remember { mutableFloatStateOf(0.8F) }
            val indicatorSize = 50.dp
            val trackWidth: Dp = (indicatorSize * .15f)

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                GradientProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(indicatorSize)
                        .align(Center),
                    strokeWidth = trackWidth,
                    gradientStart = primaryBlue,
                    gradientEnd = primaryGreen,
                    trackColor = Color.LightGray,
                )
            }

            // Bottom sheet to update calories goal
            if (showCaloriesGoalBottomSheet) {
                ShowModalBottomSheet(
                    sheetState = sheetState,
                    caloriesGoal = caloriesGoal,
                    onSheetDismissed = {
                        showCaloriesGoalBottomSheet = false
                    }
                ) {
                    caloriesGoal = it
                    progress = caloriesGoal / 5000
                }
            }
        }

        // Update calories button
        item {
            Text(
                text = "Change Calories Goal",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable {
                        showCaloriesGoalBottomSheet = true
                    },
                color = primaryFoodCardBackground,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }


        // Today's consumed dishes -
        items(listOf("Cake", "Pasta")) {
            DishesConsumedToday(dish = it)
        }
    }
}

@Composable
fun DishesConsumedToday(dish: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(text = dish, modifier = Modifier.weight(0.7f))
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.align(CenterVertically))
            Text(
                text = "5", modifier = Modifier
                    .padding(5.dp)
                    .align(CenterVertically)
            )
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.align(CenterVertically))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowModalBottomSheet(
    sheetState: SheetState,
    caloriesGoal: Float,
    onSheetDismissed: () -> Unit,
    onCaloriesChanged: (Float) -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.Black,
        onDismissRequest = {
            onSheetDismissed()
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        BoxWithConstraints(
            Modifier
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
        ) {
            Column {
                CaloriesConsumedAndSliderComposable(
                    calorieDescription = "Calories Goal : ${caloriesGoal.toInt()}",
                    shouldEnableSlider = true,
                    calories = caloriesGoal,
                    textModifier = Modifier.padding(10.dp),
                    sliderModifier = Modifier.padding(10.dp)
                ) {
                    onCaloriesChanged(it)
                }
            }
        }

    }
}

@Composable
fun CaloriesConsumedAndSliderComposable(
    calorieDescription: String,
    shouldEnableSlider: Boolean,
    calories: Float,
    textModifier: Modifier,
    sliderModifier: Modifier,
    onCaloriesChanged: (Float) -> Unit = {},
) {
    Text(
        text = calorieDescription,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        modifier = textModifier
    )

    Slider(
        value = calories,
        onValueChange = {
            onCaloriesChanged(it)
        },
        valueRange = 1000F..5000F,
        enabled = shouldEnableSlider,
        modifier = sliderModifier
    )
}


@Composable
fun GradientProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    gradientStart: Color,
    gradientEnd: Color,
    trackColor: Color,
    strokeWidth: Dp,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
    }
    Canvas(
        modifier
            .progressSemantics(progress)
    ) {
        // Start at 12 o'clock
        val startAngle = 270f
        val sweep = progress * 360f
        drawDeterminateCircularIndicator(startAngle, 360f, trackColor, stroke)
        drawCircularIndicator(
            startAngle = startAngle,
            sweep = sweep,
            gradientStart = gradientStart,
            gradientEnd = gradientEnd,
            stroke = stroke
        )
    }
}

private fun DrawScope.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) = drawCircularIndicator(startAngle, sweep, color, stroke)

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    stroke: Stroke,
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    rotate(degrees = -90f) {
        drawArc(
            brush = Brush.sweepGradient(
                colorStops = listOf(
                    0.0f to gradientStart,
                    sweep / 360 to gradientEnd,
                ).toTypedArray()
            ),
            startAngle = startAngle + 90,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
    }
}

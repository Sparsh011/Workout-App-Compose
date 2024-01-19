package com.sparshchadha.workout_app.ui.screens.workout

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sparshchadha.workout_app.ui.components.bottom_bar.BottomBarScreen
import com.sparshchadha.workout_app.ui.components.bottom_bar.UtilityScreen
import com.sparshchadha.workout_app.ui.screens.workout.yoga.YogaDifficultyLevels
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.Extensions.capitalize
import com.sparshchadha.workout_app.viewmodel.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WorkoutScreenComposable(
    gymWorkoutCategories: List<YogaDifficultyLevels>,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
    val selectedItem = remember { mutableStateOf<ImageVector?>(null) }
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomModalDrawerSheet(items = items, selectedItem = selectedItem, scope = scope, drawerState = drawerState)
        },
        content = {
            WorkoutScreen(scope = scope, gymWorkoutCategories = gymWorkoutCategories, drawerState = drawerState, workoutViewModel = workoutViewModel, navController = navController, context = context)
        }
    )
}


@Composable
fun WorkoutScreen(
    scope: CoroutineScope,
    gymWorkoutCategories: List<YogaDifficultyLevels>,
    drawerState: DrawerState,
    workoutViewModel: WorkoutViewModel,
    navController: NavController,
    context: Context
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .background(ColorsUtil.primaryBackgroundColor)
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "What Would You Like To Do Today?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(0.9f)
                )

                Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = Color.White, modifier = Modifier
                    .weight(0.1f)
                    .clickable {
                        scope.launch {
                            drawerState.open()
                        }
                    })
            }
        }

        // Gym workout
        header {
            Text(
                text = "Gym Workout",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

        items(gymWorkoutCategories) {
            WorkoutComposable(
                modifier = Modifier.padding(all = 20.dp),
                category = it,
                onWorkoutSelection = { difficultyLevel ->
//                    workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                    // navigate to gym screen
                    Toast.makeText(context, "Clicked $difficultyLevel", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Yoga poses
        header {
            Text(
                text = "Yoga Poses",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

        items(gymWorkoutCategories) {
            WorkoutComposable(
                modifier = Modifier.padding(all = 20.dp),
                category = it,
                onWorkoutSelection = { difficultyLevel ->
                    workoutViewModel.updateYogaDifficultyLevel(difficultyLevel = difficultyLevel)
                    // navigate to yoga screen
                    workoutViewModel.getYogaPoses()
                    navController.navigate(route = UtilityScreen.YogaPoses.route) {
                        popUpTo(BottomBarScreen.WorkoutScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        header {
            Text(
                text = "Track Your Today's Workout",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(all = 10.dp)
            )
        }

//        item {
//            WorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = "Gym Workout")
//        }
//
//        item {
//            WorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = "Yoga Session")
//        }
    }

}

@Composable
fun WorkoutComposable(modifier: Modifier, category: YogaDifficultyLevels, onWorkoutSelection: (YogaDifficultyLevels) -> Unit) {
    PopulateCategory(category = category, modifier = modifier, textColor = Color.White, onWorkoutSelection = onWorkoutSelection)
}

@Composable
fun PopulateCategory(category: YogaDifficultyLevels, modifier: Modifier, textColor: Color, onWorkoutSelection: (YogaDifficultyLevels) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(all = 20.dp)
            .clickable {
                onWorkoutSelection(category)
            },
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryDarkGray
        )
    ) {
        Text(
            text = category.name.lowercase().capitalize(),
            color = textColor,
            modifier = modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit,
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@Composable
fun CustomModalDrawerSheet(
    items: List<ImageVector>,
    selectedItem: MutableState<ImageVector?>,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    val context = LocalContext.current
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item, contentDescription = null) },
                label = { Text(item.name) },
                selected = item == selectedItem.value,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = item
                    Toast.makeText(context, "Selected item - ${selectedItem.value!!.name}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
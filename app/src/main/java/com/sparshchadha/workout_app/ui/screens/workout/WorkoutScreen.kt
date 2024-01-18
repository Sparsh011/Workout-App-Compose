package com.sparshchadha.workout_app.ui.screens.workout

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparshchadha.workout_app.util.ColorsUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WorkoutScreenComposable(gymWorkoutCategories: List<String>) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
    val selectedItem = remember { mutableStateOf<ImageVector?>(null) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomModalDrawerSheet(items = items, selectedItem = selectedItem, scope = scope, drawerState = drawerState)
        },
        content = {
            WorkoutScreen(scope = scope, gymWorkoutCategories = gymWorkoutCategories, drawerState = drawerState)
        }
    )
}

@Composable
fun CustomModalDrawerSheet(items: List<ImageVector>, selectedItem: MutableState<ImageVector?>, scope: CoroutineScope, drawerState: DrawerState) {
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

@Composable
fun WorkoutScreen(scope: CoroutineScope, gymWorkoutCategories: List<String>, drawerState: DrawerState) {

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
            Row (
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
            GymWorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = it)
        }

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
            GymWorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = it)
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

        item {
            GymWorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = "Gym Workout")
        }

        item {
            GymWorkoutComposable(modifier = Modifier.padding(all = 20.dp), category = "Yoga Session")
        }
    }

}

@Composable
fun GymWorkoutComposable(modifier: Modifier, category: String) {
    PopulateCategory(category = category, modifier = modifier, textColor = Color.White)
}

@Composable
fun PopulateCategory(category: String, modifier: Modifier, textColor: Color) {
    Card (
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(all = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorsUtil.primaryDarkGray
        )
    ){
        Text(text = category, color = textColor, modifier = modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun MainActPreview() {
    WorkoutScreenComposable(
        gymWorkoutCategories = listOf(
            "Beginner",
            "Intermediate",
            "Advanced",
            "Calisthenics"
        )
    )
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}
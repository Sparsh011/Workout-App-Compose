package com.sparshchadha.workout_app.ui.components.bottom_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sparshchadha.workout_app.util.ColorsUtil

@Composable
fun BottomBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.CalorieTracker,
        BottomBarScreen.WorkoutScreen,
        BottomBarScreen.ProfileScreen
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = ColorsUtil.primaryLightGray,
        contentColor = ColorsUtil.primaryBlack
    ) {
        screens.forEach { screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navHostController = navHostController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navHostController: NavHostController,
) {
    val selected = (currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true)

    NavigationBarItem(
        selected = selected,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
            if (selected) {
                Icon(
                    imageVector = screen.selectedIcon,
                    contentDescription = null,
                    tint = Color.Black
                )

            } else {
                Icon(
                    imageVector = screen.unselectedIcon,
                    contentDescription = null,
                    tint = ColorsUtil.unselectedBottomBarIconColor
                )
            }
        },
        label = {
            if (selected) {
                Text(
                    text = screen.title,
                    color = Color.Black
                )
            } else {
                Text(
                    text = screen.title,
                    color = ColorsUtil.unselectedBottomBarIconColor
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = ColorsUtil.primaryLightGray,
            selectedIconColor = Color.Black
        )
    )
}
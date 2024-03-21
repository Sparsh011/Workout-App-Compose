package com.sparshchadha.workout_app.ui.components.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sparshchadha.workout_app.util.ColorsUtil
import com.sparshchadha.workout_app.util.ColorsUtil.primaryTextColor
import com.sparshchadha.workout_app.util.Extensions.nonScaledSp

@Composable
fun BottomBar(
    navHostController: NavHostController,
    bottomBarState: Boolean
) {
    val screens = BottomBarScreens.getBottomBarScreens()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(
                containerColor = ColorsUtil.bottomBarColor,
                contentColor = ColorsUtil.scaffoldContentColor
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navHostController = navHostController
                    )
                }
            }
        }
    )

}

@Composable
fun RowScope.AddItem(
    screen: ScreenRoutes,
    currentDestination: NavDestination?,
    navHostController: NavHostController,
) {
    val selected = (currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true)

    NavigationBarItem(
        selected = selected,
        onClick = {
            if (!selected) {
                navHostController.navigate(screen.route) {
                    popUpTo(navHostController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        },
        icon = {
            if (selected) {
                Icon(
                    imageVector = screen.selectedIcon,
                    contentDescription = null,
                    tint = primaryTextColor
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
                    color = primaryTextColor,
                    fontSize = 12.nonScaledSp
                )
            } else {
                Text(
                    text = screen.title,
                    color = ColorsUtil.unselectedBottomBarIconColor,
                    fontSize = 12.nonScaledSp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White
        )
    )
}

object BottomBarScreens {
    fun getBottomBarScreens(): List<ScreenRoutes> {
        return listOf(
            ScreenRoutes.CalorieTracker,
            ScreenRoutes.WorkoutScreen,
            ScreenRoutes.RemindersScreen,
            ScreenRoutes.ProfileScreen,
        )
    }
}
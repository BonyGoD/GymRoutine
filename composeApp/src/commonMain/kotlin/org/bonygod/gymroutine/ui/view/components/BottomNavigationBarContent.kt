package org.bonygod.gymroutine.ui.view.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.bonygod.gymroutine.ui.navigation.Tabs
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun BottomNavigationBarContent(
    navController: NavHostController
) {
    BottomNavigationBarContent { item ->
        navController.navigate(item.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun BottomNavigationBarContent(
    onItemClicked: (Tabs) -> Unit
) {
    BottomAppBar(
        modifier = Modifier,
        containerColor = CustomBlack,
        contentColor = CustomYellow
    ) {
        val items = listOf(
            Tabs.TabDashboard,
            Tabs.TabRoutines,
            Tabs.TabUserProfile
        )

        var selectedItem by remember { mutableStateOf(0) }
        var currentRoute by remember { mutableStateOf(Tabs.TabDashboard.route) }

        items.forEachIndexed { index, navigationItem ->
            if (navigationItem.route == currentRoute) {
                selectedItem = index
            }
        }

        NavigationBar(
            containerColor = CustomBlack,
            contentColor = CustomYellow,
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemColors(
                        unselectedIconColor = CustomWhite,
                        selectedIconColor = CustomGray,
                        unselectedTextColor = CustomWhite,
                        selectedTextColor = CustomYellow,
                        selectedIndicatorColor = CustomYellow,
                        disabledIconColor = CustomBlack,
                        disabledTextColor = CustomWhite
                    ),
                    alwaysShowLabel = true,
                    icon = { Icon(item.icon!!, contentDescription = "") },
                    label = { Text(item.title) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        currentRoute = item.route
                        onItemClicked(item)
                    }
                )

            }
        }
    }
}


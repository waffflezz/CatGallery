package ru.sfu.waffflezz.catgallery.bottomNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(
    navController: NavController
) {
    val listItem = listOf(
        BottomItem.HomeScreenItem,
        BottomItem.CatsScreenItem,
        BottomItem.FavoriteScreenItem
    )
    NavigationBar{
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItem.forEach {item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.iconId), contentDescription = null)
                },
                label = {
                    Text(text = item.title, fontSize = 9.sp)
                },
                alwaysShowLabel = true)
        }
    }
}
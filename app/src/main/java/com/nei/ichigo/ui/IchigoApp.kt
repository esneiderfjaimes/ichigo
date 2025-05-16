package com.nei.ichigo.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.navigateToChampions
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.navigateToIcons
import com.nei.ichigo.feature.encyclopedia.settings.navigation.navigateToEncyclopediaSettings
import com.nei.ichigo.navigation.IchigoNavHost

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val action: NavController.(navOptions: NavOptions) -> Unit
) {
    data object Champions : Screen(ChampionsRoute.javaClass.name, "Champions", Icons.Default.Face, {
        navigateToChampions(it)
    })

    data object ProfileIcons : Screen(IconsRoute.javaClass.name, "Icons", Icons.Default.Image, {
        navigateToIcons(it)
    })

    data object Settings : Screen("settings", "Settings", Icons.Default.Settings, {
        navigateToEncyclopediaSettings(navOptions {
            launchSingleTop = true
        })
    })

    companion object {
        val allScreens = listOf(Champions, ProfileIcons, Settings)
    }
}


@Composable
fun IchigoApp() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            Screen.allScreens.forEach { screen ->
                item(
                    icon = {
                        Icon(
                            screen.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(screen.title) },
                    selected = currentDestination?.destination?.route == screen.route,
                    onClick = {
                        val topLevelNavOptions = navOptions {

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                        screen.action(navController, topLevelNavOptions)
                    }
                )
            }
        }
    ) {
        IchigoNavHost(navController)
    }
}
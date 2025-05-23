package com.nei.ichigo.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.nei.ichigo.R
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.navigateToChampions
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.navigateToIcons
import com.nei.ichigo.feature.encyclopedia.settings.navigation.EncyclopediaSettingsRoute
import com.nei.ichigo.feature.encyclopedia.settings.navigation.navigateToEncyclopediaSettings
import com.nei.ichigo.navigation.IchigoNavHost

sealed class Screen(
    val route: String,
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val action: NavController.() -> Unit
) {
    data object Champions : Screen(
        route = ChampionsRoute.javaClass.name,
        title = R.string.champions,
        icon = Icons.Default.Face,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToChampions(navOptions)
        }
    )

    data object ProfileIcons : Screen(
        route = IconsRoute.javaClass.name,
        title = R.string.icons,
        icon = Icons.Default.Image,
        action = {
            val navOptions = topLevelDestinationNavOptions()
            navigateToIcons(navOptions)
        }
    )

    data object Settings : Screen(
        route = EncyclopediaSettingsRoute.javaClass.name,
        title = R.string.settings,
        icon = Icons.Default.Settings,
        action = {
            navigateToEncyclopediaSettings(navOptions {
                launchSingleTop = true
            })
        }
    )

    companion object {
        val allScreens = listOf(Champions, ProfileIcons, Settings)
    }
}

fun NavController.topLevelDestinationNavOptions() = navOptions {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }

    launchSingleTop = true
    restoreState = true
}

/**
 * check [androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo]
 */
@Composable
@Suppress("DEPRECATION")
fun calculateFromAdaptiveInfo(): NavigationSuiteType {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    return with(adaptiveInfo) {
        if (
            windowPosture.isTabletop ||
            windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
        ) {
            NavigationSuiteType.NavigationBar
        } else if (
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM
        ) {
            NavigationSuiteType.WideNavigationRailCollapsed
        } else if (
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
        ) {
            NavigationSuiteType.WideNavigationRailExpanded
            // NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteType.NavigationBar
        }
    }
}

@Composable
fun IchigoApp() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val navSuiteType = calculateFromAdaptiveInfo()
    NavigationSuiteScaffold(
        navigationSuiteType = navSuiteType,
        /*
        layoutType = navSuiteType,
        navigationSuite = {
            // Custom Navigation Rail with centered items.
            if (navSuiteType == NavigationSuiteType.NavigationRail) {
                NavigationRail {
                    // Adding Spacers before and after the item so they are pushed towards the
                    // center of the NavigationRail.
                    Spacer(Modifier.weight(1f))
                    Screen.allScreens.forEach { screen ->
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(screen.title)) },
                            selected = currentDestination?.destination?.route == screen.route,
                            onClick = { screen.action(navController) }
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
            } else {
                NavigationSuite {
                    Screen.allScreens.forEach { screen ->
                        item(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(screen.title)) },
                            selected = currentDestination?.destination?.route == screen.route,
                            onClick = { screen.action(navController) }
                        )
                    }
                }
            }
        }
            */
        navigationItems = {
            Screen.allScreens.forEach { screen ->
                NavigationSuiteItem(
                    navigationSuiteType = navSuiteType,
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(screen.title)) },
                    selected = currentDestination?.destination?.route == screen.route,
                    onClick = { screen.action(navController) }
                )
            }
        }
    ) {
        IchigoNavHost(navController)
    }
}
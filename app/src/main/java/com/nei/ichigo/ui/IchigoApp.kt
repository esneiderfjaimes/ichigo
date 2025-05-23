package com.nei.ichigo.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.nei.ichigo.navigation.IchigoNavHost
import com.nei.ichigo.navigation.Screen

/**
 * check [androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo]
 */
@Composable
@Suppress("DEPRECATION")
fun calculateFromAdaptiveInfo(): NavigationSuiteType {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    return with(adaptiveInfo) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
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
            NavigationSuiteType.NavigationRail
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
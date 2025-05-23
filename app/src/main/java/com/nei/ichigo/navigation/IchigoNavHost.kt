package com.nei.ichigo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.nei.ichigo.R
import com.nei.ichigo.feature.encyclopedia.champions.navigation.ChampionsRoute
import com.nei.ichigo.feature.encyclopedia.champions.navigation.navigateToChampions
import com.nei.ichigo.feature.encyclopedia.champions2pane.championsListDetail
import com.nei.ichigo.feature.encyclopedia.icons.navigation.IconsRoute
import com.nei.ichigo.feature.encyclopedia.icons.navigation.icons
import com.nei.ichigo.feature.encyclopedia.icons.navigation.navigateToIcons
import com.nei.ichigo.feature.encyclopedia.settings.navigation.EncyclopediaSettingsRoute
import com.nei.ichigo.feature.encyclopedia.settings.navigation.encyclopediaSettings
import com.nei.ichigo.feature.encyclopedia.settings.navigation.navigateToEncyclopediaSettings

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
            navigateToEncyclopediaSettings(navOptions { launchSingleTop = true })
        }
    )

    companion object {
        val allScreens = listOf(Champions, ProfileIcons, Settings)
    }
}

private fun NavController.topLevelDestinationNavOptions() = navOptions {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }

    launchSingleTop = true
    restoreState = true
}

@Composable
fun IchigoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = IconsRoute,
    ) {
        championsListDetail()
        icons()
        encyclopediaSettings(onDismiss = { navController.popBackStack() })
    }
}